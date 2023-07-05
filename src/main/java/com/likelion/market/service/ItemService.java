package com.likelion.market.service;

import com.likelion.market.repository.ItemRepository;
import com.likelion.market.dto.ItemDto;
import com.likelion.market.entity.ItemEntity;
import com.likelion.market.entity.ItemStatus;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class ItemService {
    private final ItemRepository repository;

    // 물품 정보 - 등록
    public ItemDto createItem(ItemDto dto) {
        ItemEntity newItem = new ItemEntity();
        newItem.setTitle(dto.getTitle());
        newItem.setDescription(dto.getDescription());
        newItem.setWriter(dto.getWriter());
        newItem.setPassword(dto.getPassword());
        newItem.setStatus(ItemStatus.FOR_SALE);
        newItem.setMinPriceWanted(dto.getMinPriceWanted());
        repository.save(newItem);
        return ItemDto.fromEntity(newItem);
    }

    // 물품 정보 - 단일 조회
    public ItemDto readItem(Long id) {
        Optional<ItemEntity> optionalItem = repository.findById(id);
        if (optionalItem.isPresent())
            return ItemDto.fromEntity(optionalItem.get());
        else throw new ResponseStatusException(HttpStatus.NOT_FOUND);
    }

    // 물품 정보 - 페이지 단위 조회
    public Page<ItemDto> readItemPaged(
            Integer pageNumber, Integer pageSize
    ) {
        Pageable pageable = PageRequest.of(
                pageNumber, pageSize, Sort.by("id").descending()
        );
        Page<ItemEntity> itemEntityPage = repository.findAll(pageable);
        Page<ItemDto> itemDtoPage = itemEntityPage.map(ItemDto::fromEntity);
        return itemDtoPage;
    }

    // 물품 정보 - 전체 조회 -> 페이지 단위 조회와 중복이므로 주석 처리
//    public List<ItemDto> readItemAll() {
//        List<ItemDto> itemList = new ArrayList<>();
//        for (ItemEntity entity: repository.findAll()) {
//            itemList.add(ItemDto.fromEntity(entity));
//        }
//        return itemList;
//    }

    // 물품 정보 - 수정
    public ItemDto updateItem(Long id, ItemDto dto) {
        Optional<ItemEntity> optionalItem = repository.findById(id);
        if (optionalItem.isPresent()) {
            ItemEntity item = optionalItem.get();
            if (!item.getPassword().equals(dto.getPassword())) {
                throw new ResponseStatusException(
                        HttpStatus.UNAUTHORIZED, "비밀번호가 일치하지 않습니다."
                );
            }
            item.setTitle(dto.getTitle());
            item.setDescription(dto.getDescription());
            item.setWriter(dto.getWriter());
            item.setPassword(dto.getPassword());
            item.setStatus(dto.getStatus());
            item.setMinPriceWanted(dto.getMinPriceWanted());
            repository.save(item);
            return ItemDto.fromEntity(item);
        }
        else throw new ResponseStatusException(HttpStatus.NOT_FOUND);
    }

    // 물품 정보 - 수정 - 이미지
    public ItemDto updateItemImage(Long id, MultipartFile itemImage) {
        Optional<ItemEntity> optionalItem = repository.findById(id);
        if (optionalItem.isEmpty())
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);

        // 폴더만 생성
        String imageDir = String.format("image/%d/", id);
        log.info(imageDir);
        try {
            Files.createDirectories(Path.of(imageDir));
        } catch (IOException e) {
            log.error(e.getMessage());
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR);
        }

        // 확장자를 포함한 이미지 이름 생성
        String originalFilename = itemImage.getOriginalFilename();
        String[] fileNameSplit = originalFilename.split("\\.");
        String extension = fileNameSplit[fileNameSplit.length - 1];
        String imageFilename = "image." + extension;
        log.info(imageFilename);

        // 폴더와 파일 경로를 포함한 이름 생성
        String imagePath = imageDir + imageFilename;
        log.info(imagePath);

        // MultipartFile 저장
        try {
            itemImage.transferTo(Path.of(imagePath));
        } catch (IOException e) {
            log.error(e.getMessage());
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR);
        }

        log.info(String.format("/static/%d/%s", id, imageFilename));

        ItemEntity userEntity = optionalItem.get();
        userEntity.setImageUrl(String.format("/static/%d/%s", id, imageFilename));
        repository.save(userEntity);
        return ItemDto.fromEntity(repository.save(userEntity));
    }

    // 물품 정보 - 수정 - 상태
    public void updateItemStatus(Long itemId, ItemStatus status) {
        ItemEntity item = repository.findById(itemId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        item.setStatus(status);
        repository.save(item);
    }

    // 물품 정보 - 삭제
    public void deleteItem(Long id, ItemDto dto) {
        Optional<ItemEntity> optionalItem = repository.findById(id);
        if (optionalItem.isPresent()) {
            ItemEntity item = optionalItem.get();
            if (!item.getPassword().equals(dto.getPassword())) {
                throw new ResponseStatusException(
                        HttpStatus.UNAUTHORIZED, "비밀번호가 일치하지 않습니다."
                );
            }
            if (!item.getWriter().equals(dto.getWriter())) {
                throw new ResponseStatusException(
                        HttpStatus.UNAUTHORIZED, "작성자가 일치하지 않습니다."
                );
            }
            repository.deleteById(id);
        }
        else throw new ResponseStatusException(HttpStatus.NOT_FOUND);
    }
}

package com.likelion.market.service;

import com.likelion.market.dto.ItemDto;
import com.likelion.market.dto.ItemPageDto;
import com.likelion.market.dto.ItemReadDto;
import com.likelion.market.entity.ItemEntity;
import com.likelion.market.entity.ItemStatus;
import com.likelion.market.entity.UserEntity;
import com.likelion.market.repository.ItemRepository;
import com.likelion.market.repository.UserRepository;
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
    private final ItemRepository itemRepository;
    private final UserRepository userRepository;

    // 물품 정보 - 등록
    public ItemDto createItem(ItemDto dto) {
        if (dto.getUserId() == null || !userRepository.existsById(dto.getUserId())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "유효하지 않은 사용자입니다.");
        }
        UserEntity user = userRepository.findById(dto.getUserId()).
                orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "해당 사용자를 찾을 수 없습니다."));
        ItemEntity item = dto.newEntity(user);
        item.setUser(user);
        itemRepository.save(item);
        return ItemDto.fromEntity(item);
    }

    // 물품 정보 - 단일 조회
    public ItemReadDto readItem(Long id) {
        Optional<ItemEntity> optionalItem = itemRepository.findById(id);
        if (optionalItem.isPresent()) {
            return ItemReadDto.fromEntity(optionalItem.get());
        }
        else throw new ResponseStatusException(HttpStatus.NOT_FOUND, "해당 물품을 찾을 수 없습니다.");
    }

    // 물품 정보 - 페이지 단위 조회
    public Page<ItemPageDto> readItemPaged(
            Integer page, Integer limit
    ) {
        Pageable pageable = PageRequest.of(
                page, limit, Sort.by("id").descending()
        );
        Page<ItemEntity> itemEntityPage = itemRepository.findAll(pageable);
        Page<ItemPageDto> itemPageDtoPage = itemEntityPage.map(ItemPageDto::fromEntity);
        return itemPageDtoPage;
    }

    // 물품 정보 - 수정
    public ItemDto updateItem(Long id, ItemDto dto) {
        Optional<ItemEntity> optionalItem = itemRepository.findById(id);
        if (optionalItem.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "해당 물품을 찾을 수 없습니다.");
        }
        ItemEntity item = optionalItem.get();
        Optional<UserEntity> optionalUser = userRepository.findById(dto.getUserId());
        if (optionalUser.isEmpty() || !item.getUser().equals(optionalUser.get())) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "물품을 등록한 작성자의 정보가 일치하지 않습니다.");
        }
        item.setTitle(dto.getTitle());
        item.setDescription(dto.getDescription());
        item.setStatus(dto.getStatus());
        item.setMinPriceWanted(dto.getMinPriceWanted());
        itemRepository.save(item);
        return ItemDto.fromEntity(item);
    }

    // 물품 정보 - 수정 - 이미지
    public ItemDto updateItemImage(Long id, MultipartFile itemImage, UserEntity requestUser) {
        Optional<ItemEntity> optionalItem = itemRepository.findById(id);
        if (optionalItem.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "해당 물품을 찾을 수 없습니다.");
        }
        ItemEntity item = optionalItem.get();
        if (!item.getUser().equals(requestUser)) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "물품을 등록한 작성자의 정보가 일치하지 않습니다.");
        }
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
        item.setImageUrl(String.format("/static/%d/%s", id, imageFilename));
        itemRepository.save(item);
        return ItemDto.fromEntity(item);
    }

    // 물품 정보 - 수정 - 상태
    public void updateItemStatus(Long itemId, ItemStatus status) {
        Optional<ItemEntity> optionalItem = itemRepository.findById(itemId);
        if (optionalItem.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "해당 물품을 찾을 수 없습니다.");
        }
        ItemEntity item = optionalItem.get();
        item.setStatus(status);
        itemRepository.save(item);
    }

    // 물품 정보 - 삭제
    public void deleteItem(Long id, UserEntity requestUser) {
        Optional<ItemEntity> optionalItem = itemRepository.findById(id);
        if (optionalItem.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "해당 물품을 찾을 수 없습니다.");
        }
        ItemEntity item = optionalItem.get();
        if (!item.getUser().equals(requestUser)) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "물품을 등록한 작성자의 정보가 일치하지 않습니다.");
        }
        itemRepository.deleteById(id);
    }
}

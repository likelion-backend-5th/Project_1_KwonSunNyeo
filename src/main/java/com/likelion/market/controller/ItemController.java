package com.likelion.market.controller;

import com.likelion.market.dto.*;
import com.likelion.market.entity.UserEntity;
import com.likelion.market.repository.UserRepository;
import com.likelion.market.service.ItemService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Optional;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/items")
public class ItemController {
    private final ItemService service;
    private final UserRepository userRepository;

    // 물품 정보 - 등록
    // POST /items
    @PostMapping
    public ResponseEntity<ResponseDto> create(
            @RequestBody ItemDto dto
    ) {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (principal instanceof UserDetails) {
            String username = ((UserDetails) principal).getUsername();
            Optional<UserEntity> userOptional = userRepository.findByUsername(username);
            if (userOptional.isPresent()) {
                UserEntity user = userOptional.get();
                dto.setUserId(user.getId());
                service.createItem(dto);
                ResponseDto response = new ResponseDto();
                response.setMessage("등록이 완료되었습니다.");
                response.setStatus(200);
                return new ResponseEntity<>(response, HttpStatus.OK);
            }
        }
        ResponseDto response = new ResponseDto();
        response.setMessage("작성자 정보를 찾을 수 없습니다.");
        response.setStatus(400);
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    // 물품 정보 - 단일 조회
    // GET /items/{itemId}
    @GetMapping("/{id}")
    public ResponseEntity<ItemReadDto> read(@PathVariable("id") Long id) {
        ItemReadDto item = service.readItem(id);
        return new ResponseEntity<>(item, HttpStatus.OK);
    }

    // 물품 정보 - 페이지 단위 조회
    // GET /items?page={page}&limit={limit}
    @GetMapping
    public Page<ItemPageDto> readAll(
            @RequestParam(value = "page", defaultValue = "0") Integer page,
            @RequestParam(value = "limit", defaultValue = "25") Integer limit
    ) {
        return service.readItemPaged(page, limit);
    }

    // 물품 정보 - 수정
    // PUT /items/{itemId}
    @PutMapping("/{id}")
    public ResponseEntity<MessageResponseDto> update(
            @PathVariable("id") Long id,
            @RequestBody ItemDto dto
    ) {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (principal instanceof UserDetails) {
            String username = ((UserDetails) principal).getUsername();
            Optional<UserEntity> userOptional = userRepository.findByUsername(username);
            if (userOptional.isPresent()) {
                UserEntity user = userOptional.get();
                dto.setUserId(user.getId());
                service.updateItem(id, dto);
                MessageResponseDto response = new MessageResponseDto();
                response.setMessage("물품이 수정되었습니다.");
                return new ResponseEntity<>(response, HttpStatus.OK);
            }
        }
        MessageResponseDto response = new MessageResponseDto();
        response.setMessage("작성자 정보를 찾을 수 없습니다.");
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    // 물품 정보 - 수정 - 이미지
    // PUT /items/{itemId}/image
    @PutMapping(
            value = "/{id}/image",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE
    )
    public ResponseEntity<MessageResponseDto> image(
            @PathVariable("id") Long id,
            @RequestParam("image") MultipartFile itemImage
    ) {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (principal instanceof UserDetails) {
            String username = ((UserDetails) principal).getUsername();
            Optional<UserEntity> userOptional = userRepository.findByUsername(username);
            if (userOptional.isPresent()) {
                UserEntity user = userOptional.get();
                service.updateItemImage(id, itemImage, user);
                MessageResponseDto response = new MessageResponseDto();
                response.setMessage("이미지가 등록되었습니다.");
                return new ResponseEntity<>(response, HttpStatus.OK);
            }
        }
        MessageResponseDto response = new MessageResponseDto();
        response.setMessage("작성자 정보를 찾을 수 없습니다.");
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    // 물품 정보 - 삭제
    // DELETE /items/{itemId}
    @DeleteMapping("/{id}")
    public ResponseEntity<MessageResponseDto> delete(
            @PathVariable("id") Long id,
            @RequestBody ItemDto dto
    ) {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (principal instanceof UserDetails) {
            String username = ((UserDetails) principal).getUsername();
            Optional<UserEntity> userOptional = userRepository.findByUsername(username);
            if (userOptional.isPresent()) {
                UserEntity user = userOptional.get();
                service.deleteItem(id, user);
                MessageResponseDto response = new MessageResponseDto();
                response.setMessage("물품을 삭제했습니다.");
                return new ResponseEntity<>(response, HttpStatus.OK);
            }
        }
        MessageResponseDto response = new MessageResponseDto();
        response.setMessage("작성자 정보를 찾을 수 없습니다.");
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }
}

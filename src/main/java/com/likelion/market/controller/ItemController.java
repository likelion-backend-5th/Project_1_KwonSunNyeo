package com.likelion.market.controller;

import com.likelion.market.dto.ItemDto;
import com.likelion.market.dto.MessageResponseDto;
import com.likelion.market.dto.ResponseDto;
import com.likelion.market.service.ItemService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/items")
public class ItemController {
    private final ItemService service;

    // 물품 정보 - 등록
    // POST /items
    @PostMapping
    public ResponseEntity<ResponseDto> create(@RequestBody ItemDto dto) {
        service.createItem(dto);
        ResponseDto response = new ResponseDto();
        response.setMessage("등록이 완료되었습니다.");
        response.setStatus(200);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    // 물품 정보 - 단일 조회
    // GET /items/{itemId}
    @GetMapping("/{id}")
    public ResponseEntity<ItemDto> read(@PathVariable("id") Long id) {
        ItemDto item = service.readItem(id);
        return new ResponseEntity<>(item, HttpStatus.OK);
    }

    // 물품 정보 - 페이지 단위 조회
    // GET /items?page={page}&limit={limit}
    @GetMapping
    public Page<ItemDto> readAll(
            @RequestParam(value = "page", defaultValue = "0") Integer page,
            @RequestParam(value = "limit", defaultValue = "20") Integer limit
    ) {
        return service.readItemPaged(page, limit);
    }

    // 물품 정보 - 전체 조회 -> 페이지 단위 조회와 중복이므로 주석 처리
//    // GET /items
//    @GetMapping
//    public List<ItemDto> readAll() {
//        return service.readItemAll();
//    }

    // 물품 정보 - 수정
    // PUT /items/{itemId}
    @PutMapping("/{id}")
    public ResponseEntity<MessageResponseDto> update(
            @PathVariable("id") Long id,
            @RequestBody ItemDto dto
    ) {
        service.updateItem(id, dto);
        MessageResponseDto response = new MessageResponseDto();
        response.setMessage("물품이 수정되었습니다.");
        return new ResponseEntity<>(response, HttpStatus.OK);
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
        service.updateItemImage(id, itemImage);
        MessageResponseDto response = new MessageResponseDto();
        response.setMessage("이미지가 등록되었습니다.");
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    // 물품 정보 - 삭제
    // DELETE /items/{itemId}
    @DeleteMapping("/{id}")
    public ResponseEntity<MessageResponseDto> delete(
            @PathVariable("id") Long id,
            @RequestBody ItemDto dto
    ) {
        service.deleteItem(id, dto);
        MessageResponseDto response = new MessageResponseDto();
        response.setMessage("물품을 삭제했습니다.");
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}

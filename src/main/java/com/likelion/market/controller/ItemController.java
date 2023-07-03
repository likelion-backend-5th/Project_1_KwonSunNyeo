package com.likelion.market.controller;

import com.likelion.market.dto.ItemDto;
import com.likelion.market.service.ItemService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/items")
public class ItemController {
    private final ItemService service;

    // 물품 정보 - 등록
    // POST /items
    @PostMapping
    public ItemDto create(@RequestBody ItemDto dto) {
        return service.createItem(dto);
    }

    // 물품 정보 - 단일 조회
    // GET /items/{itemId}
    @GetMapping("/{id}")
    public ItemDto read(@PathVariable("id") Long id) {
        return service.readItem(id);
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
    public ItemDto update(
            @PathVariable("id") Long id,
            @RequestBody ItemDto dto
    ) {
        return service.updateItem(id, dto);
    }

    // 물품 정보 - 삭제
    // DELETE /items/{itemId}
    @DeleteMapping("/{id}")
    public void delete(
            @PathVariable("id") Long id,
            @RequestBody ItemDto dto
    ) {
        service.deleteItem(id, dto);
    }
}

package com.likelion.market.controller;

import com.likelion.market.dto.CommentDto;
import com.likelion.market.service.CommentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/items/{itemId}/comments")
public class CommentController {
    private final CommentService service;

    // 물품 댓글 - 등록
    // POST /items/{itemId}/comments
    @PostMapping
    public CommentDto create(
            @PathVariable("itemId") Long itemId,
            @RequestBody CommentDto dto
    ) {
        return service.createComment(itemId, dto);
    }

    // 물품 댓글 - 페이지 단위 조회
    // GET /items/{itemId}/comments
    @GetMapping
    public Page<CommentDto> readAll(
            @PathVariable("itemId") Long itemId,
            @RequestParam(name = "page", defaultValue = "0") Integer page,
            @RequestParam(name = "size", defaultValue = "10") Integer size
    ) {
        return service.readCommentPaged(itemId, page, size);
    }

    // 물품 댓글 - 전체 조회 -> 페이지 단위 조회와 중복이므로 주석 처리
//    // GET /items/{itemId}/comments
//    @GetMapping
//    public List<CommentDto> readAll(
//            @PathVariable("itemId") Long itemId
//    ) {
//        return service.readCommentAll(itemId);
//    }

    // 물품 댓글 - 수정
    // PUT /items/{itemId}/comments/{commentId}
    @PutMapping("/{commentId}")
    public CommentDto update(
            @PathVariable("itemId") Long itemId,
            @PathVariable("commentId") Long commentId,
            @RequestBody CommentDto dto
    ) {
        return service.updateComment(itemId, commentId, dto);
    }

    // 물품 댓글 - 삭제
    // DELETE /items/{itemId}/comments/{commentId}
    @DeleteMapping("/{commentId}")
    public void delete(
            @PathVariable("itemId") Long itemId,
            @PathVariable("commentId") Long commentId,
            @RequestBody CommentDto dto
    ) {
        service.deleteComment(itemId, commentId, dto);
    }
}

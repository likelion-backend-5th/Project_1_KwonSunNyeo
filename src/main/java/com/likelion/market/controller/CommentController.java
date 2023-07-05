package com.likelion.market.controller;

import com.likelion.market.dto.CommentDto;
import com.likelion.market.dto.CommentPageDto;
import com.likelion.market.dto.MessageResponseDto;
import com.likelion.market.dto.ResponseDto;
import com.likelion.market.service.CommentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
    public ResponseEntity<ResponseDto> create(
            @PathVariable("itemId") Long itemId,
            @RequestBody CommentDto dto
    ) {
        service.createComment(itemId, dto);
        ResponseDto response = new ResponseDto();
        response.setMessage("댓글이 등록되었습니다.");
        response.setStatus(200);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    // 물품 댓글 - 페이지 단위 조회
    // GET /items/{itemId}/comments
    @GetMapping
    public Page<CommentPageDto> readAll(
            @PathVariable("itemId") Long itemId,
            @PageableDefault(size = 25, sort = "id", direction = Sort.Direction.DESC) Pageable pageable
    ) {
        return service.readCommentPaged(itemId, pageable.getPageNumber(), pageable.getPageSize());
    }

    // 물품 댓글 - 수정
    // PUT /items/{itemId}/comments/{commentId}
    @PutMapping("/{commentId}")
    public ResponseEntity<MessageResponseDto> update(
            @PathVariable("itemId") Long itemId,
            @PathVariable("commentId") Long commentId,
            @RequestBody CommentDto dto
    ) {
        service.updateComment(itemId, commentId, dto);
        MessageResponseDto response = new MessageResponseDto();
        response.setMessage("댓글이 수정되었습니다.");
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    // 물품 댓글 - 답글
    // PUT /items/{itemId}/comments/{commentId}/reply
    @PutMapping("/{commentId}/reply")
    public ResponseEntity<MessageResponseDto> updateReply(
            @PathVariable("itemId") Long itemId,
            @PathVariable("commentId") Long commentId,
            @RequestBody CommentDto dto
    ) {
        service.updateReply(itemId, commentId, dto);
        MessageResponseDto response = new MessageResponseDto();
        response.setMessage("댓글에 답변이 추가되었습니다.");
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    // 물품 댓글 - 삭제
    // DELETE /items/{itemId}/comments/{commentId}
    @DeleteMapping("/{commentId}")
    public ResponseEntity<MessageResponseDto> delete(
            @PathVariable("itemId") Long itemId,
            @PathVariable("commentId") Long commentId,
            @RequestBody CommentDto dto
    ) {
        service.deleteComment(itemId, commentId, dto);
        MessageResponseDto response = new MessageResponseDto();
        response.setMessage("댓글을 삭제했습니다.");
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}

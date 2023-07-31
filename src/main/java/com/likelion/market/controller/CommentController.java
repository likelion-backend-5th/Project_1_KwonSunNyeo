package com.likelion.market.controller;

import com.likelion.market.dto.CommentDto;
import com.likelion.market.dto.CommentPageDto;
import com.likelion.market.dto.ResponseDto;
import com.likelion.market.entity.UserEntity;
import com.likelion.market.repository.UserRepository;
import com.likelion.market.service.CommentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/items/{itemId}/comments")
public class CommentController {
    private final CommentService service;
    private final UserRepository userRepository;

    // 물품 댓글 - 등록
    // POST /items/{itemId}/comments
    @PostMapping
    public ResponseEntity<ResponseDto> create(
            @PathVariable("itemId") Long itemId,
            @RequestBody CommentDto dto
    ) {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (principal instanceof UserDetails) {
            String username = ((UserDetails) principal).getUsername();
            Optional<UserEntity> userOptional = userRepository.findByUsername(username);
            if (userOptional.isPresent()) {
                UserEntity user = userOptional.get();
                dto.setUserId(user.getId());
                dto.setItemId(itemId);
                service.createComment(itemId, dto);
                ResponseDto response = new ResponseDto();
                response.setMessage("댓글이 등록되었습니다.");
                return new ResponseEntity<>(response, HttpStatus.OK);
            }
        }
        ResponseDto response = new ResponseDto();
        response.setMessage("작성자 정보를 찾을 수 없습니다.");
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
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
    public ResponseEntity<ResponseDto> update(
            @PathVariable("itemId") Long itemId,
            @PathVariable("commentId") Long commentId,
            @RequestBody CommentDto dto
    ) {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (principal instanceof UserDetails) {
            String username = ((UserDetails) principal).getUsername();
            Optional<UserEntity> userOptional = userRepository.findByUsername(username);
            if (userOptional.isPresent()) {
                UserEntity user = userOptional.get();
                dto.setUserId(user.getId());
                dto.setItemId(itemId);
                dto.setId(commentId);
                service.updateComment(itemId, commentId, dto);
                ResponseDto response = new ResponseDto();
                response.setMessage("댓글이 수정되었습니다.");
                return new ResponseEntity<>(response, HttpStatus.OK);
            }
        }
        ResponseDto response = new ResponseDto();
        response.setMessage("작성자 정보를 찾을 수 없습니다.");
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    // 물품 댓글 - 답글
    // PUT /items/{itemId}/comments/{commentId}/reply
    @PutMapping("/{commentId}/reply")
    public ResponseEntity<ResponseDto> updateReply(
            @PathVariable("itemId") Long itemId,
            @PathVariable("commentId") Long commentId,
            @RequestBody CommentDto dto
    ) {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (principal instanceof UserDetails) {
            String username = ((UserDetails) principal).getUsername();
            Optional<UserEntity> userOptional = userRepository.findByUsername(username);
            if (userOptional.isPresent()) {
                UserEntity user = userOptional.get();
                dto.setUserId(user.getId());
                dto.setItemId(itemId);
                dto.setId(commentId);
                service.updateReply(itemId, commentId, dto);
                ResponseDto response = new ResponseDto();
                response.setMessage("댓글에 답변이 추가되었습니다.");
                return new ResponseEntity<>(response, HttpStatus.OK);
            }
        }
        ResponseDto response = new ResponseDto();
        response.setMessage("작성자 정보를 찾을 수 없습니다.");
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    // 물품 댓글 - 삭제
    // DELETE /items/{itemId}/comments/{commentId}
    @DeleteMapping("/{commentId}")
    public ResponseEntity<ResponseDto> delete(
            @PathVariable("itemId") Long itemId,
            @PathVariable("commentId") Long commentId,
            @RequestBody CommentDto dto
    ) {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (principal instanceof UserDetails) {
            String username = ((UserDetails) principal).getUsername();
            Optional<UserEntity> userOptional = userRepository.findByUsername(username);
            if (userOptional.isPresent()) {
                UserEntity user = userOptional.get();
                dto.setUserId(user.getId());
                dto.setItemId(itemId);
                dto.setId(commentId);
                service.deleteComment(itemId, commentId, dto);
                ResponseDto response = new ResponseDto();
                response.setMessage("댓글이 삭제되었습니다.");
                return new ResponseEntity<>(response, HttpStatus.OK);
            }
        }
        ResponseDto response = new ResponseDto();
        response.setMessage("작성자 정보를 찾을 수 없습니다.");
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }
}

package com.likelion.market.service;

import com.likelion.market.dto.CommentDto;
import com.likelion.market.dto.CommentPageDto;
import com.likelion.market.entity.CommentEntity;
import com.likelion.market.entity.ItemEntity;
import com.likelion.market.entity.UserEntity;
import com.likelion.market.repository.CommentRepository;
import com.likelion.market.repository.ItemRepository;
import com.likelion.market.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CommentService {
    private final ItemRepository itemRepository;
    private final CommentRepository commentRepository;
    private final UserRepository userRepository;

    private static final String INVALID_USER = "유효하지 않은 사용자입니다.";
    private static final String NOT_FOUND_ITEM = "해당 물품을 찾을 수 없습니다.";
    private static final String NOT_FOUND_USER = "해당 사용자를 찾을 수 없습니다.";
    private static final String NOT_FOUND_COMMENT = "해당 댓글을 찾을 수 없습니다.";
    private static final String UNAUTHORIZED_USER = "댓글을 등록한 작성자의 정보가 일치하지 않습니다.";
    private static final String UNAUTHORIZED_ITEM_OWNER = "물품을 등록한 작성자의 정보가 일치하지 않습니다.";


    // 물품 댓글 - 등록
    public CommentDto createComment(Long itemId, CommentDto dto) {
        if (dto.getUserId() == null || !userRepository.existsById(dto.getUserId())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, INVALID_USER);
        }
        ItemEntity item = itemRepository.findById(itemId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, NOT_FOUND_ITEM));
        UserEntity user = userRepository.findById(dto.getUserId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, NOT_FOUND_USER));
        CommentEntity newComment = dto.newEntity(user, item);
        commentRepository.save(newComment);
        return CommentDto.fromEntity(newComment);
    }

    // 물품 댓글 - 페이지 단위 조회
    public Page<CommentPageDto> readCommentPaged(
            Long itemId, Integer page, Integer limit
    ) {
        Pageable pageable = PageRequest.of(
                page, limit, Sort.by("id").descending()
        );
        Page<CommentEntity> commentEntityPage = commentRepository.findAllByItemId(itemId, pageable);
        Page<CommentPageDto> commentPageDtoPage = commentEntityPage.map(CommentPageDto::fromEntity);
        return commentPageDtoPage;
    }

    // 물품 댓글 - 수정
    public CommentDto updateComment(Long itemId, Long commentId, CommentDto dto) {
        CommentEntity comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, NOT_FOUND_COMMENT));
        UserEntity user = userRepository.findById(dto.getUserId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, NOT_FOUND_USER));
        if (!comment.getUser().equals(user)) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, UNAUTHORIZED_USER);
        }
        comment.update(dto);
        commentRepository.save(comment);
        return CommentDto.fromEntity(comment);
    }

    // 물품 댓글 - 답변
    public CommentDto updateReply(Long itemId, Long commentId, CommentDto dto) {
        CommentEntity comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, NOT_FOUND_COMMENT));
        UserEntity user = userRepository.findById(dto.getUserId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, NOT_FOUND_USER));
        ItemEntity item = itemRepository.findById(itemId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, NOT_FOUND_ITEM));
        if (!item.getUser().equals(user)) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, UNAUTHORIZED_ITEM_OWNER);
        }
        comment.setReply(dto.getReply());
        commentRepository.save(comment);
        return CommentDto.fromEntity(comment);
    }

    // 물품 댓글 - 삭제
    public void deleteComment(Long itemId, Long commentId, Long userId) {
        CommentEntity comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, NOT_FOUND_COMMENT));
        UserEntity user = userRepository.findById(userId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, NOT_FOUND_USER));
        if (!comment.getUser().equals(user)) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, UNAUTHORIZED_USER);
        }
        commentRepository.deleteById(commentId);
    }
}

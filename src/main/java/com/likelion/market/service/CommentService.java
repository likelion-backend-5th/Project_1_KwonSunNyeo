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

    // 물품 댓글 - 등록
    public CommentDto createComment(Long itemId, CommentDto dto) {
        if (dto.getUserId() == null || !userRepository.existsById(dto.getUserId())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid user ID");
        }
        ItemEntity item = itemRepository.findById(itemId).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        UserEntity user = userRepository.findById(dto.getUserId()).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
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
        Optional<ItemEntity> optionalItem = itemRepository.findById(itemId);
        if (optionalItem.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
        Optional<CommentEntity> optionalComment = commentRepository.findById(commentId);
        if (optionalComment.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
        CommentEntity comment = optionalComment.get();
        if (!comment.getUser().equals(userRepository.findById(dto.getUserId()).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND)))) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "댓글을 등록한 작성자가 일치하지 않습니다.");
        }
        comment.setContent(dto.getContent());
        commentRepository.save(comment);
        return CommentDto.fromEntity(comment);
    }

    // 물품 댓글 - 답변
    public CommentDto updateReply(Long itemId, Long commentId, CommentDto dto) {
        Optional<ItemEntity> optionalItem = itemRepository.findById(itemId);
        if (optionalItem.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
        Optional<CommentEntity> optionalComment = commentRepository.findById(commentId);
        if (optionalComment.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
        CommentEntity comment = optionalComment.get();
        if (!comment.getUser().equals(userRepository.findById(dto.getUserId()).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND)))) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "댓글을 등록한 작성자가 일치하지 않습니다.");
        }
        comment.setReply(dto.getReply());
        commentRepository.save(comment);
        return CommentDto.fromEntity(comment);
    }

    // 물품 댓글 - 삭제
    public void deleteComment(Long itemId, Long commentId, CommentDto dto) {
        Optional<CommentEntity> optionalComment = commentRepository.findById(commentId);
        if (optionalComment.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
        CommentEntity comment = optionalComment.get();
        if (!comment.getUser().equals(userRepository.findById(dto.getUserId()).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND)))) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "댓글을 등록한 작성자가 일치하지 않습니다.");
        }
        commentRepository.deleteById(commentId);
    }
}

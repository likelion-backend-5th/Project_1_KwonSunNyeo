package com.likelion.market.service;

import com.likelion.market.entity.ItemEntity;
import com.likelion.market.repository.CommentRepository;
import com.likelion.market.repository.ItemRepository;
import com.likelion.market.dto.CommentDto;
import com.likelion.market.entity.CommentEntity;
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

    // 물품 댓글 - 등록
    public CommentDto createComment(Long itemId, CommentDto dto) {
        if (!itemRepository.existsById(itemId))
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        CommentEntity newComment = new CommentEntity();
        newComment.setItemId(itemId);
        newComment.setWriter(dto.getWriter());
        newComment.setPassword(dto.getPassword());
        newComment.setContent(dto.getContent());
        commentRepository.save(newComment);
        return CommentDto.fromEntity(newComment);
    }

    // 물품 댓글 - 페이지 단위 조회
    public Page<CommentDto> readCommentPaged(
            Long itemId, Integer pageNumber, Integer pageSize
    ) {
        Pageable pageable = PageRequest.of(
                pageNumber, pageSize, Sort.by("id").descending()
        );
        Page<CommentEntity> commentEntityPage = commentRepository.findAllByItemId(itemId, pageable);
        Page<CommentDto> commentDtoPage = commentEntityPage.map(CommentDto::fromEntity);
        return commentDtoPage;
    }

    // 물품 댓글 - 전체 조회 -> 페이지 단위 조회와 중복이므로 주석 처리
//    public List<CommentDto> readCommentAll(Long itemId) {
//        List<CommentEntity> commentEntities
//                = commentRepository.findAllByItemId(itemId);
//        List<CommentDto> commentList = new ArrayList<>();
//        for (CommentEntity entity: commentEntities) {
//            commentList.add(CommentDto.fromEntity(entity));
//        }
//        return commentList;
//    }

    // 물품 댓글 - 수정
    public CommentDto updateComment(Long itemId, Long commentId, CommentDto dto) {
        Optional<CommentEntity> optionalComment = commentRepository.findById(commentId);
        if (optionalComment.isEmpty())
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        CommentEntity comment = optionalComment.get();
        if (!itemId.equals(comment.getItemId()))
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        if (!comment.getPassword().equals(dto.getPassword())) {
            throw new ResponseStatusException(
                    HttpStatus.UNAUTHORIZED, "비밀번호가 일치하지 않습니다."
            );
        }
        comment.setContent(dto.getContent());
        comment.setWriter(dto.getWriter());
        commentRepository.save(comment);
        return CommentDto.fromEntity(comment);
    }

    // 물품 댓글 - 답변
    public CommentDto updateReply(Long itemId, Long commentId, CommentDto dto) {
        Optional<ItemEntity> optionalItem = itemRepository.findById(itemId);
        if (optionalItem.isEmpty())
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        Optional<CommentEntity> optionalComment = commentRepository.findById(commentId);
        if (optionalComment.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
        CommentEntity comment = optionalComment.get();
        if (!itemId.equals(comment.getItemId()))
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        if (!optionalItem.get().getPassword().equals(dto.getPassword())) {
            throw new ResponseStatusException(
                    HttpStatus.UNAUTHORIZED, "비밀번호가 일치하지 않습니다."
            );
        }
        comment.setReply(dto.getReply());
        commentRepository.save(comment);
        return CommentDto.fromEntity(comment);
    }

    // 물품 댓글 - 삭제
    public void deleteComment(Long itemId, Long commentId, CommentDto dto) {
        Optional<CommentEntity> optionalComment = commentRepository.findById(commentId);
        if (optionalComment.isEmpty())
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        CommentEntity comment = optionalComment.get();
        if (!itemId.equals(comment.getItemId()))
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        if (!comment.getPassword().equals(dto.getPassword())) {
            throw new ResponseStatusException(
                    HttpStatus.UNAUTHORIZED, "비밀번호가 일치하지 않습니다."
            );
        }
        if (!comment.getWriter().equals(dto.getWriter())) {
            throw new ResponseStatusException(
                    HttpStatus.UNAUTHORIZED, "작성자가 일치하지 않습니다."
            );
        }
        commentRepository.deleteById(commentId);
    }
}
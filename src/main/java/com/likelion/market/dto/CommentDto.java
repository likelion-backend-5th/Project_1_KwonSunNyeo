package com.likelion.market.dto;

import com.likelion.market.entity.CommentEntity;
import com.likelion.market.entity.ItemEntity;
import com.likelion.market.entity.UserEntity;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class CommentDto {
    private Long id;
    private Long itemId;
    private Long userId;
    private String password;

    @NotBlank(message = "내용은 필수입니다.")
    private String content;

    private String reply;

    public static CommentDto fromEntity(CommentEntity entity) {
        CommentDto dto = new CommentDto();
        dto.setId(entity.getId());
        dto.setItemId(entity.getItem().getId());
        dto.setUserId(entity.getUser().getId());
        dto.setPassword(entity.getPassword());
        dto.setContent(entity.getContent());
        dto.setReply(entity.getReply());
        return dto;
    }

    public CommentEntity newEntity(UserEntity user, ItemEntity item) {
        CommentEntity entity = new CommentEntity();
        entity.setItem(item);
        entity.setUser(user);
        entity.setPassword(password);
        entity.setContent(content);
        entity.setReply(reply);
        return entity;
    }
}

package com.likelion.market.dto;

import com.likelion.market.entity.CommentEntity;
import com.likelion.market.entity.ItemEntity;
import com.likelion.market.entity.UserEntity;
import lombok.Data;

@Data
public class CommentDto {
    private Long id;
    private Long itemId;
    private Long userId;
    private String password;
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
        entity.setUser(user);
        entity.setItem(item);
        entity.setPassword(password);
        entity.setContent(content);
        entity.setReply(reply);
        return entity;
    }
}

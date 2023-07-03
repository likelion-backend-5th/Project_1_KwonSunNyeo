package com.likelion.market.dto;

import com.likelion.market.entity.ItemEntity;
import com.likelion.market.entity.ItemStatus;
import lombok.Data;

@Data
public class ItemDto {
    private Long id;
    private String title;
    private String description;
    private String imageUrl;
    private int minPriceWanted;
    private String writer;
    private String password;
    private ItemStatus status;

    public static ItemDto fromEntity(ItemEntity entity) {
        ItemDto dto = new ItemDto();
        dto.setId(entity.getId());
        dto.setTitle(entity.getTitle());
        dto.setDescription(entity.getDescription());
        dto.setImageUrl(entity.getImageUrl());
        dto.setMinPriceWanted(entity.getMinPriceWanted());
        dto.setWriter(entity.getWriter());
        dto.setPassword(entity.getPassword());
        dto.setStatus(entity.getStatus());
        return dto;
    }
}

package com.likelion.market.dto;

import com.likelion.market.entity.ItemEntity;
import com.likelion.market.entity.ItemStatus;
import lombok.Data;

@Data
public class ItemPageDto {
    private Long id;
    private String title;
    private String description;
    private int minPriceWanted;
    private String imageUrl;
    private ItemStatus status;

    public static ItemPageDto fromEntity(ItemEntity entity) {
        ItemPageDto dto = new ItemPageDto();
        dto.setId(entity.getId());
        dto.setTitle(entity.getTitle());
        dto.setDescription(entity.getDescription());
        dto.setMinPriceWanted(entity.getMinPriceWanted());
        dto.setImageUrl(entity.getImageUrl());
        dto.setStatus(entity.getStatus());
        return dto;
    }
}

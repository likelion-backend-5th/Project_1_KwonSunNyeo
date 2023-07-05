package com.likelion.market.dto;

import com.likelion.market.entity.ItemEntity;
import com.likelion.market.entity.ItemStatus;
import lombok.Data;

@Data
public class ItemReadDto {
    private String title;
    private String description;
    private int minPriceWanted;
    private ItemStatus status;

    public static ItemReadDto fromEntity(ItemEntity entity) {
        ItemReadDto dto = new ItemReadDto();
        dto.setTitle(entity.getTitle());
        dto.setDescription(entity.getDescription());
        dto.setMinPriceWanted(entity.getMinPriceWanted());
        dto.setStatus(entity.getStatus());
        return dto;
    }
}

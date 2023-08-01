package com.likelion.market.dto;

import com.likelion.market.entity.ItemEntity;
import com.likelion.market.entity.ItemStatus;
import com.likelion.market.entity.UserEntity;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class ItemDto {
    private Long id;

    @NotBlank(message = "상품 제목은 필수입니다.")
    private String title;

    @NotBlank(message = "상품 설명은 필수입니다.")
    private String description;

    private String imageUrl;

    @NotNull(message = "가격은 필수입니다.")
    @Min(value = 0, message = "가격은 0 이상이어야 합니다.")
    private Integer minPriceWanted;

    private Long userId;
    private String password;
    private ItemStatus status;

    public static ItemDto fromEntity(ItemEntity entity) {
        ItemDto dto = new ItemDto();
        dto.setId(entity.getId());
        dto.setTitle(entity.getTitle());
        dto.setDescription(entity.getDescription());
        dto.setImageUrl(entity.getImageUrl());
        dto.setMinPriceWanted(entity.getMinPriceWanted());
        dto.setUserId(entity.getUser().getId());
        dto.setPassword(entity.getPassword());
        dto.setStatus(entity.getStatus());
        return dto;
    }

    public ItemEntity newEntity(UserEntity user) {
        ItemEntity entity = new ItemEntity();
        entity.setTitle(title);
        entity.setDescription(description);
        entity.setImageUrl(imageUrl);
        entity.setMinPriceWanted(minPriceWanted);
        entity.setUser(user);
        entity.setStatus(ItemStatus.FOR_SALE);
        return entity;
    }
}

package com.likelion.market.dto;

import com.likelion.market.entity.ProposalEntity;
import com.likelion.market.entity.ProposalStatus;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class ProposalDto {
    private Long id;
    private Long itemId;
    private Long userId;

    @NotNull(message = "가격은 필수입니다.")
    @Min(value = 0, message = "가격은 0 이상이어야 합니다.")
    private Integer suggestedPrice;

    private String writer;
    private String password;
    private ProposalStatus status;

    public static ProposalDto fromEntity(ProposalEntity entity) {
        ProposalDto dto = new ProposalDto();
        dto.setId(entity.getId());
        dto.setItemId(entity.getItem().getId());
        dto.setUserId(entity.getUser().getId());
        dto.setSuggestedPrice(entity.getSuggestedPrice());
        dto.setWriter(entity.getUser().getUsername());
        dto.setStatus(entity.getStatus());
        return dto;
    }
}

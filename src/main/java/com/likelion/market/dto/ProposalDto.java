package com.likelion.market.dto;

import com.likelion.market.entity.ProposalEntity;
import com.likelion.market.entity.ProposalStatus;
import lombok.Data;

@Data
public class ProposalDto {
    private Long id;
    private Long itemId;
    private int suggestedPrice;
    private String writer;
    private String password;
    private ProposalStatus status;

    public static ProposalDto fromEntity(ProposalEntity entity) {
        ProposalDto dto = new ProposalDto();
        dto.setId(entity.getId());
        dto.setItemId(entity.getItem().getId());
        dto.setSuggestedPrice(entity.getSuggestedPrice());
        dto.setWriter(entity.getUser().getUsername());
        dto.setStatus(entity.getStatus());
        return dto;
    }
}

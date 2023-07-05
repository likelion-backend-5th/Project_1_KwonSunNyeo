package com.likelion.market.dto;

import com.likelion.market.entity.ProposalEntity;
import com.likelion.market.entity.ProposalStatus;
import lombok.Data;

@Data
public class ProposalPageDto {
    private Long id;
    private int suggestedPrice;
    private ProposalStatus status;

    public static ProposalPageDto fromEntity(ProposalEntity entity) {
        ProposalPageDto dto = new ProposalPageDto();
        dto.setId(entity.getId());
        dto.setSuggestedPrice(entity.getSuggestedPrice());
        dto.setStatus(entity.getStatus());
        return dto;
    }
}

package com.likelion.market.entity;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "proposals")
public class ProposalEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 제안은 한 상품에서 나올 수 있다.
    @ManyToOne
    @JoinColumn(name = "item_id")
    private ItemEntity item;

    private Integer suggestedPrice;

    // 제안은 한 사용자에게 나올 수 있다.
    @ManyToOne
    @JoinColumn(name = "user_id")
    private UserEntity user;

    private String password;

    @Enumerated(EnumType.STRING)
    private ProposalStatus status;

    public void updatePrice(Integer suggestedPrice) {
        this.suggestedPrice = suggestedPrice;
    }

    public void updateStatus(ProposalStatus status) {
        this.status = status;
    }
}

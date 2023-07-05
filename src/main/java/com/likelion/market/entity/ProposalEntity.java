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
    private Long itemId;
    private int suggestedPrice;
    private String writer;
    private String password;

    @Enumerated(EnumType.STRING)
    private ProposalStatus status;
}

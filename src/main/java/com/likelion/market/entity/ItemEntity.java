package com.likelion.market.entity;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "items")
public class ItemEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String title;
    private String description;
    private int minPriceWanted;
    private String imageUrl;
    private String writer;
    private String password;

    @Enumerated(EnumType.STRING)
    private ItemStatus status;
}

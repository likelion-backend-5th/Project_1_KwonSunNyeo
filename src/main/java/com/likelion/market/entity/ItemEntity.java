package com.likelion.market.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

@Data
@Entity
@Table(name = "items")
public class ItemEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String title;
    private String description;
    private Integer minPriceWanted;
    private String imageUrl;

    // 상품은 한 사용자에게 속할 수 있다.
    @ManyToOne
    @JoinColumn(name = "user_id")
    private UserEntity user;

    private String password;

    @Enumerated(EnumType.STRING)
    private ItemStatus status;

    // 상품은 여러 제안을 받을 수 있다.
    @OneToMany(mappedBy = "item")
    private List<ProposalEntity> proposals;

    // 상품은 여러 댓글을 가질 수 있다.
    @OneToMany(mappedBy = "item")
    private List<CommentEntity> comments;
}

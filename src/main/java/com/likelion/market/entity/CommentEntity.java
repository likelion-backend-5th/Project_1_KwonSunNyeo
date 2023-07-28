package com.likelion.market.entity;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "comments")
public class CommentEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 댓글은 한 상품에서 나올 수 있다.
    @ManyToOne
    @JoinColumn(name = "item_id")
    private ItemEntity item;

    // 댓글은 한 사용자에게 나올 수 있다.
    @ManyToOne
    @JoinColumn(name = "user_id")
    private UserEntity user;

    private String password;
    private String content;
    private String reply;
}

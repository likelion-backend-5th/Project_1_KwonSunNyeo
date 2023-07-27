package com.likelion.market.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "users")
@Data
public class UserEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 필수 입력
    @Column(nullable = false, unique = true)
    private String username;
    @Column(nullable = false)
    private String password;

    // 선택 입력
    private String phone;
    private String email;
    private String address;
}

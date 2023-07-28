package com.likelion.market.entity;

import com.likelion.market.domain.CustomUserDetails;
import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

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

    // 사용자는 여러 상품을 가질 수 있다.
    @OneToMany(mappedBy = "user")
    private List<ItemEntity> items;

    // 사용자는 여러 댓글을 작성할 수 있다.
    @OneToMany(mappedBy = "user")
    private List<CommentEntity> comments;

    // 사용자는 여러 제안을 할 수 있다.
    @OneToMany(mappedBy = "user")
    private List<ProposalEntity> proposals;

    @Enumerated(EnumType.STRING)
    private Role role = Role.USER; // 기본값

    // UserEntity 에서 CustomUserDetails 생성
    public static CustomUserDetails fromEntity(UserEntity entity) {
        return CustomUserDetails.builder()
                .id(entity.getId())
                .username(entity.getUsername())
                .password(entity.getPassword())
                .phone(entity.getPhone())
                .email(entity.getEmail())
                .address(entity.getAddress())
                .role(entity.getRole())
                .build();
    }
}

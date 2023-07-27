package com.likelion.market.repository;

import com.likelion.market.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<UserEntity, Long> {
    // 사용자 이름으로 사용자를 찾는 메서드
    Optional<UserEntity> findByUsername(String username);
    // 사용자 이름으로 사용자 존재 여부를 확인하는 메서드
    boolean existsByUsername(String username);
}

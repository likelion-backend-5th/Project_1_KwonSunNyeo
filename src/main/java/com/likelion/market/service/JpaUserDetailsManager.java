package com.likelion.market.service;

import com.likelion.market.domain.CustomUserDetails;
import com.likelion.market.entity.UserEntity;
import com.likelion.market.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

@Slf4j
@Service
public class JpaUserDetailsManager implements UserDetailsManager {
    private final UserRepository userRepository;

    public JpaUserDetailsManager(
            UserRepository userRepository,
            PasswordEncoder passwordEncoder
    ) {
        this.userRepository = userRepository;
        createUser(CustomUserDetails.builder()
                .username("user")
                .password(passwordEncoder.encode("asdf"))
                .phone("010-1234-5678")
                .email("user@gmail.com")
                .address("지구 어딘가")
                .build());
    }

    @Override
    // 사용자 이름을 통해 사용자 정보를 로드하는 메서드
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<UserEntity> optionalUser
                = userRepository.findByUsername(username);
        if (optionalUser.isEmpty())
            throw new UsernameNotFoundException(username);
        return CustomUserDetails.fromEntity(optionalUser.get());
    }

    @Override
    // 사용자 이름을 통해 새로운 사용자를 저장하는 메서드
    public void createUser(UserDetails user) {
        log.info("try create user: {}", user.getUsername());
        // 사용자가 이미 존재한다면 생성할 수 없다.
        if (this.userExists(user.getUsername()))
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        try {
            userRepository.save(
                    ((CustomUserDetails) user).newEntity());
        } catch (ClassCastException e) {
            log.error("failed to cast to {}", CustomUserDetails.class);
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    // 사용자 이름을 통해 사용자의 존재 여부를 확인하는 메서드
    public boolean userExists(String username) {
        log.info("check if user: {} exists", username);
        return this.userRepository.existsByUsername(username);
    }

    @Override
    // 사용자 정보 업데이트 메서드 (현재 사용하지 않음)
    public void updateUser(UserDetails user) {
    }

    @Override
    // 사용자 삭제 메서드 (현재 사용하지 않음)
    public void deleteUser(String username) {
    }

    @Override
    // 비밀번호 변경 메서드 (현재 사용하지 않음)
    public void changePassword(String oldPassword, String newPassword) {
    }
}

package com.likelion.market.dto;

import com.likelion.market.domain.CustomUserDetails;
import com.likelion.market.entity.Role;
import lombok.Data;
import org.springframework.security.crypto.password.PasswordEncoder;

@Data
public class UserDto {
    private String username;
    private String password;
    private String passwordCheck;
    private String phone;
    private String email;
    private String address;
    private Role role;

    public CustomUserDetails toCustomUserDetails(PasswordEncoder passwordEncoder) {
        if (!this.password.equals(this.passwordCheck)) {
            throw new IllegalArgumentException("Password does not match!");
        }
        return CustomUserDetails.builder()
                .username(this.username)
                .password(passwordEncoder.encode(this.password))
                .phone(this.phone)
                .email(this.email)
                .address(this.address)
                .role(Role.USER)
                .build();
    }
}

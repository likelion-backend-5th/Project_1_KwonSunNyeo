package com.likelion.market.dto;

import com.likelion.market.domain.CustomUserDetails;
import com.likelion.market.entity.Role;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import org.springframework.security.crypto.password.PasswordEncoder;

@Data
public class UserDto {
    @NotBlank(message = "아이디는 필수입니다.")
    private String username;

    @NotBlank(message = "비밀번호는 필수입니다.")
    private String password;

    @NotBlank(message = "비밀번호 확인은 필수입니다.")
    private String passwordCheck;

    private String phone;
    private String email;
    private String address;
    private Role role;

    public CustomUserDetails toCustomUserDetails(PasswordEncoder passwordEncoder) {
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

package com.likelion.market.controller;

import com.likelion.market.domain.CustomUserDetails;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Slf4j
@Controller
@RequestMapping("users")
public class UserController {
    // 로그인 폼 반환
    @GetMapping("/login")
    public String loginForm() {
        return "login-form";
    }

    // 마이 프로필 페이지 반환
    @GetMapping("/my-profile")
    public String myProfile(
            Authentication authentication
    ) {
        // 현재 접속중인 아이디 출력
        CustomUserDetails userDetails
                = (CustomUserDetails) authentication.getPrincipal();
        log.info(userDetails.getUsername());
        log.info(userDetails.getPhone());
        log.info(userDetails.getEmail());
        log.info(userDetails.getAddress());
        return "my-profile";
    }

    // 회원가입 폼 반환
    @GetMapping("/register")
    public String registerForm() {
        return "register-form";
    }

    private final UserDetailsManager manager;
    private final PasswordEncoder passwordEncoder;

    public UserController(
            UserDetailsManager manager,
            PasswordEncoder passwordEncoder
    ) {
        this.manager = manager;
        this.passwordEncoder = passwordEncoder;
    }

    // 회원가입 요청 처리
    @PostMapping("/register")
    public String registerPost(
            @RequestParam("username") String username,
            @RequestParam("password") String password,
            @RequestParam("password-check") String passwordCheck
    ) {
        // 입력받은 비밀번호와 비밀번호 확인 값이 일치하는 경우, 사용자 생성 및 로그인 페이지로 이동
        if (password.equals(passwordCheck)) {
            log.info("password match!");
            manager.createUser(CustomUserDetails.builder()
                    .username(username)
                    .password(passwordEncoder.encode(password))
                    .build());
            return "redirect:/users/login";
        }
        // 일치하지 않은 경우, 회원가입 페이지로 이동
        log.warn("password does not match...");
        return "redirect:/users/register?error";
    }
}

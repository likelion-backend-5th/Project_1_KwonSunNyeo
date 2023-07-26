package com.likelion.market.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

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
    public String myProfile() {
        return "my-profile";
    }

    // 회원가입 폼 반환
    @GetMapping("/register")
    public String registerForm() {
        return "register-form";
    }
}

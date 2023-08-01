package com.likelion.market.controller;

import com.likelion.market.domain.CustomUserDetails;
import com.likelion.market.dto.ResponseDto;
import com.likelion.market.dto.UserDto;
import com.likelion.market.dto.UserProfileDto;
import com.likelion.market.jwt.JwtRequestDto;
import com.likelion.market.jwt.JwtTokenDto;
import com.likelion.market.jwt.JwtTokenUtils;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@Slf4j
@Controller
@RequestMapping("users")
public class UserController {

    // 회원가입 폼 반환
    @GetMapping("/register")
    public String registerForm() {
        return "register-form";
    }

    // 로그인 폼 반환
    @GetMapping("/login")
    public String loginForm() {
        return "login-form";
    }

    // 마이 프로필 페이지 반환
    @GetMapping("/my-profile")
    public ResponseEntity<UserProfileDto> myProfile(
            Authentication authentication
    ) {
        // 현재 접속중인 아이디 출력
        CustomUserDetails userDetails
                = (CustomUserDetails) authentication.getPrincipal();
        UserProfileDto profile = new UserProfileDto();
        profile.setUsername(userDetails.getUsername());
        profile.setPhone(userDetails.getPhone());
        profile.setEmail(userDetails.getEmail());
        profile.setAddress(userDetails.getAddress());
        return new ResponseEntity<>(profile, HttpStatus.OK);
    }

    private final UserDetailsManager manager;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenUtils jwtTokenUtils;

    public UserController(
            UserDetailsManager manager,
            PasswordEncoder passwordEncoder,
            JwtTokenUtils jwtTokenUtils
    ) {
        this.manager = manager;
        this.passwordEncoder = passwordEncoder;
        this.jwtTokenUtils = jwtTokenUtils;
    }

    // 회원가입 요청 처리
    @PostMapping("/register")
    public ResponseEntity<ResponseDto> register(
            @Valid @RequestBody UserDto userDto
    ) {
        try {
            if(manager.userExists(userDto.getUsername())) {
                ResponseDto response = new ResponseDto();
                response.setMessage("이미 존재하는 아이디입니다.");
                return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
            }
            if(!userDto.getPassword().equals(userDto.getPasswordCheck())){
                ResponseDto response = new ResponseDto();
                response.setMessage("비밀번호가 일치하지 않습니다.");
                return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
            }
            CustomUserDetails userDetails = userDto.toCustomUserDetails(passwordEncoder);
            manager.createUser(userDetails);
            ResponseDto response = new ResponseDto();
            response.setMessage("회원가입이 완료되었습니다.");
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            ResponseDto response = new ResponseDto();
            response.setMessage("회원가입이 실패되었습니다.");
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // 로그인 요청 처리
    @PostMapping("/login")
    public ResponseEntity<ResponseDto> login(
            @RequestBody JwtRequestDto requestDto
    ) {
        try {
            if (requestDto.getUsername().trim().isEmpty() || requestDto.getPassword().trim().isEmpty()) {
                ResponseDto response = new ResponseDto();
                response.setMessage("사용자 정보를 찾을 수 없습니다.");
                return new ResponseEntity<>(response, HttpStatus.UNAUTHORIZED);
            }
            UserDetails userDetails = null;
            try {
                userDetails = manager.loadUserByUsername(requestDto.getUsername());
            } catch (UsernameNotFoundException e) {
                ResponseDto response = new ResponseDto();
                response.setMessage("사용자 정보를 찾을 수 없습니다.");
                return new ResponseEntity<>(response, HttpStatus.UNAUTHORIZED);
            }
            if (!passwordEncoder.matches(requestDto.getPassword(), userDetails.getPassword())) {
                ResponseDto response = new ResponseDto();
                response.setMessage("비밀번호가 일치하지 않습니다.");
                return new ResponseEntity<>(response, HttpStatus.UNAUTHORIZED);
            }
            JwtTokenDto jwtToken = new JwtTokenDto();
            jwtToken.setToken(jwtTokenUtils.generateToken(userDetails));
            ResponseDto response = new ResponseDto();
            response.setMessage("로그인 되었습니다. 토큰은 " + jwtToken.getToken() + " 입니다.");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            ResponseDto response = new ResponseDto();
            response.setMessage("로그인이 실패되었습니다.");
            return new ResponseEntity<>(response, HttpStatus.UNAUTHORIZED);
        }
    }

    // 인증이 필요한 URL
    // 사용자가 정상적으로 인증된 경우에만 접근 가능
    @PostMapping("/check")
    public ResponseEntity<ResponseDto> check() {
        log.info(SecurityContextHolder.getContext().getAuthentication().getName());
        ResponseDto response = new ResponseDto();
        response.setMessage("인증되었습니다.");
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}

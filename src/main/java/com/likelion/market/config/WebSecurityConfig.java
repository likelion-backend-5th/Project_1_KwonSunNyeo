package com.likelion.market.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class WebSecurityConfig {
    @Bean
    public SecurityFilterChain securityFilterChain(
            HttpSecurity http
    ) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                // 인증 설정
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/no-auth", "/token/issue")
                        .permitAll() // 모든 사용자 접근 가능
                        .requestMatchers("/re-auth", "/users/my-profile")
                        .authenticated() // 인증된 사용자만 접근 가능
                        .requestMatchers("/", "/users/register")
                        .anonymous() // 인증되지 않은 사용자만 접근 가능
                )
//                // 로그인 기능 설정
//                .formLogin(formLogin -> formLogin
//                        .loginPage("/users/login") // 로그인 페이지
//                        .defaultSuccessUrl("/users/my-profile") // 로그인 성공 시
//                        .failureUrl("/users/login?fail") // 로그인 실패 시
//                        .permitAll() // 모든 사용자 접근 가능
//                )
//                // 로그아웃 기능 설정
//                .logout(logout -> logout
//                        .logoutUrl("/users/logout") // 로그아웃 페이지
//                        .logoutSuccessUrl("/users/login") // 로그아웃 성공 시
//                );
                // JWT 인증 방식을 사용하기 위한 세션 설정
                .sessionManagement(
                        sessionManagement -> sessionManagement
                                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                );
        return http.build();
    }

    // 사용자 관리
//    @Bean
    public UserDetailsManager userDetailsManager(
            PasswordEncoder passwordEncoder
    ) {
        UserDetails user = User.withUsername("user")
                .password(passwordEncoder.encode("password"))
                .build();
        return new InMemoryUserDetailsManager(user);
    }

    // 비밀번호 암호화
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}

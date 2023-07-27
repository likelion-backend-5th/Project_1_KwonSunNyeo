package com.likelion.market.jwt;

import com.likelion.market.domain.CustomUserDetails;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.ArrayList;

@Slf4j
@Component
// 사용자가 헤더에 포함한 JWT 를 해석하고 사용자가 인증된 상태인지를 확인하는 용도
public class JwtTokenFilter extends OncePerRequestFilter {
    private final JwtTokenUtils jwtTokenUtils;

    public JwtTokenFilter(JwtTokenUtils jwtTokenUtils) {
        this.jwtTokenUtils = jwtTokenUtils;
    }

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain
    ) throws ServletException, IOException {
        // JWT 가 포함되어 있으면 포함되어 있는 헤더를 요청
        String authHeader
                = request.getHeader(HttpHeaders.AUTHORIZATION);
        // authHeader 가 null 이 아니면서 "Bearer " 로 구성되어 있어야 정상적인 인증 정보
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            // JWT 를 회수하여 JWT 가 정상적인 JWT 인지 판단
            String token = authHeader.split(" ")[1];
            if (jwtTokenUtils.validate(token)) {
                SecurityContext context
                        = SecurityContextHolder.createEmptyContext();
                // JWT 에서 사용자 이름 가져오기
                String username = jwtTokenUtils
                        .parseClaims(token)
                        .getSubject();
                // 사용자 인증 정보 생성
                AbstractAuthenticationToken authenticationToken
                        = new UsernamePasswordAuthenticationToken(
                        CustomUserDetails.builder()
                                .username(username)
                                .build(),
                        token, new ArrayList<>()
                );
                // SecurityContext 에 사용자 정보 설정
                context.setAuthentication(authenticationToken);
                // SecurityContextHolder 에 SecurityContext 설정
                SecurityContextHolder.setContext(context);
                log.info("set security context with jwt");
            }
            else {
                log.warn("jwt validation failed");
            }
        }
        filterChain.doFilter(request, response);
    }
}

package com.example.na0th.auction.config;

import com.example.na0th.auction.common.config.SecretKeyConfig;
import com.example.na0th.auction.common.properties.JwtProperties;
import com.example.na0th.auction.common.util.JwtTokenProvider;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@TestConfiguration
public class TestAuthConfig {
    @Bean
    public OncePerRequestFilter accessTokenFilter() {
        return new OncePerRequestFilter() {
            @Override
            protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
                    throws ServletException, IOException {
                // 필터 검증 생략, 모든 요청 통과
                filterChain.doFilter(request, response);
            }
        };
    }
    //테스트용 시큐리티 필터 체인..
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http, OncePerRequestFilter accessTokenFilter) throws Exception {
        return http.csrf(csrf -> csrf.disable())
                .addFilterBefore(accessTokenFilter, UsernamePasswordAuthenticationFilter.class) // Mock 필터 등록
                .authorizeHttpRequests(auth -> auth.anyRequest().permitAll()) // 모든 요청 허용
                .build();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig) throws Exception {
        return authConfig.getAuthenticationManager();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecretKeyConfig secretKeyConfig() {
        return new SecretKeyConfig(); // SecretKeyConfig 빈 생성
    }

    @Bean
    public JwtProperties jwtProperties() {
        JwtProperties jwtProperties = new JwtProperties();
        jwtProperties.setAccessTokenExpiration(3600000L); // 테스트용 값 설정
        jwtProperties.setRefreshTokenExpiration(86400000L); // 테스트용 값 설정
        return jwtProperties;
    }

    @Bean
    public JwtTokenProvider jwtTokenProvider(SecretKeyConfig secretKeyConfig, JwtProperties jwtProperties) {
        return new JwtTokenProvider(secretKeyConfig, jwtProperties);
    }
}

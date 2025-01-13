package com.example.na0th.auction.common.config;

import jakarta.servlet.Filter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import java.util.Collections;

@Configuration
@Profile("no-security") // "nosec" 프로파일일 때만 활성화
public class NoOpSecurityConfig /* WebSecurityConfigurerAdapter를 상속하지 않아도 됨 */ {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * 모든 요청을 허용하는 보안 필터 체인
     */
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                // CSRF 보호를 비활성화
                .csrf(csrf -> csrf.disable())
                //h2-console
                .headers(headers -> headers.frameOptions(frameOptions -> frameOptions.sameOrigin()))
                // 모든 요청을 인증 없이 허용
                .authorizeHttpRequests(auth -> auth.anyRequest().permitAll())
                .httpBasic(httpBasic -> httpBasic.disable()); // HTTP Basic 인증 비활성화

        return http.build();
    }

    /**
     * 아무 인증도 수행하지 않는 기본 AuthenticationManager 빈
     */
    @Bean
    public AuthenticationManager authenticationManager() {
        // No-Op AuthenticationProvider: 모든 인증을 승인함.
        AuthenticationProvider noOpProvider = new AuthenticationProvider() {
            @Override
            public Authentication authenticate(Authentication authentication) throws AuthenticationException {
                // 인증 성공 처리: 인증 상태를 true로 설정하고 그대로 반환
                authentication.setAuthenticated(true);
                return authentication;
            }

            @Override
            public boolean supports(Class<?> authentication) {
                // 모든 인증 타입 지원
                return true;
            }
        };

        return new ProviderManager(Collections.singletonList(noOpProvider));
    }
}

package com.example.na0th.auction.domain.auth.filter;

import com.example.na0th.auction.common.util.JwtTokenProvider;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Profile;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Optional;

@Component
@Profile("security")
@RequiredArgsConstructor
public class AccessTokenFilter extends OncePerRequestFilter {
    private final JwtTokenProvider tokenProvider;

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        String uri = request.getRequestURI();
        // Access Token 필터를 거치지 않아야 할 경로 => 그냥 무조건 허용할 uri
        return uri.equals("/api/auth/login") ||
                uri.equals("/api/auth/reissue") || // reissue는 컨트롤러에서 처리할 것
                uri.equals("/api/v1/users") ||
                uri.startsWith("/h2-console/");
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String accessToken = resolveToken(request)
                .orElseThrow(() -> new AuthenticationException("Authentication Exception: Missing Jwt Token") {});
        //access token 검증 후, 적절하지 않으면 예외 처리 됨
        tokenProvider.validateAccessToken(accessToken);

        // 인증 객체에서 사용자 정보 추출 및 설정
        Authentication authentication = tokenProvider.getAuthentication(accessToken);
        SecurityContextHolder.getContext().setAuthentication(authentication);

        filterChain.doFilter(request, response);

    }

    // 헤더에서 "Bearer " 뒤 토큰만 추출
    private Optional<String> resolveToken(HttpServletRequest request) {
        String authHeader = request.getHeader("Authorization");
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            return Optional.of(authHeader.substring(7));
        }
        return Optional.empty();
    }
}

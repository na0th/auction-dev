package com.example.na0th.auction.common.util;

import com.example.na0th.auction.common.config.SecretKeyConfig;
import com.example.na0th.auction.common.properties.JwtProperties;
import com.example.na0th.auction.domain.user.model.MyUserDetails;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.SignatureException;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Collections;
import java.util.Date;
import java.util.List;

@Component
@Getter
@Slf4j
public class JwtTokenProvider {
    private final SecretKeyConfig secretKeyConfig;
    private final JwtProperties jwtProperties;

    private final SecretKey secretKey;
    private final long accessTokenExpiration;
    private final long refreshTokenExpiration;

    public JwtTokenProvider(SecretKeyConfig secretKeyConfig, JwtProperties jwtProperties) {
        this.secretKeyConfig = secretKeyConfig;
        this.jwtProperties = jwtProperties;

        secretKey = secretKeyConfig.getSecretKey();
        accessTokenExpiration = jwtProperties.getAccessTokenExpiration();
        refreshTokenExpiration = jwtProperties.getRefreshTokenExpiration();
    }


    // 액세스 토큰 생성
    public String generateAccessToken(Long userId) {
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + accessTokenExpiration);

        return Jwts.builder()
                .subject(String.valueOf(userId))
                .issuedAt(now)
                .expiration(expiryDate)
                .signWith(secretKey)
                .compact();
    }

    // 리프레시 토큰 생성
    public String generateRefreshToken() {
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + refreshTokenExpiration);

        return Jwts.builder()
                .subject("")
                .issuedAt(now)
                .expiration(expiryDate)
                .signWith(secretKey)
                .compact();
    }

    // userId 추출
    public Long getUserId(String token) {
        return Long.parseLong(Jwts.parser()
                .verifyWith(secretKey)
                .build()
                .parseSignedClaims(token)
                .getPayload()
                .getSubject()
        );
    }

    public Authentication getAuthentication(String token) {
        Claims claims = parseClaims(token);
        Long userId = Long.valueOf(claims.getSubject()); // userId 기준으로 유저 식별
        // 권한 정보 추출 (필요 시 확장 가능)
        List<GrantedAuthority> authorities = Collections.emptyList();
        MyUserDetails principal = MyUserDetails.from(userId);
        return new UsernamePasswordAuthenticationToken(principal, token, authorities);
    }

    private Claims parseClaims(String token) {
        return Jwts.parser()
                .verifyWith(secretKey)
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    // 토큰 유효성 검사
    private void validateToken(String token, String tokenType) {
        try {
            Jwts.parser()
                    .verifyWith(secretKey)
                    .build()
                    .parseSignedClaims(token);
        } catch (ExpiredJwtException e) {
            log.info("Expired {} token.", tokenType, e);
            throw new JwtException("Expired " + tokenType + " token.", e);
        } catch (MalformedJwtException e) {
            log.info("Malformed {} token.", tokenType, e);
            throw new JwtException("Malformed " + tokenType + " token.", e);
        } catch (SignatureException e) {
            log.error("Invalid {} token signature.", tokenType, e);
            throw new JwtException("Invalid " + tokenType + " token signature.", e);
        } catch (UnsupportedJwtException e) {
            log.info("Unsupported {} token.", tokenType, e);
            throw new JwtException("Unsupported " + tokenType + " token.", e);
        } catch (IllegalArgumentException e) {
            log.info("{} token claims string is empty.", tokenType, e);
            throw new JwtException(tokenType + " token claims string is empty.", e);
        } catch (Exception e) {
            log.info("Authentication failed: caused by {} token.", tokenType, e);
            throw new JwtException("Authentication failed: caused by " + tokenType + " token.", e);
        }
    }

    public void validateAccessToken(String token) {
        try{
            validateToken(token, "access");
        }catch (JwtException e) {
            throw new AuthenticationException("JWT validation failed: " + e.getMessage(), e) {};
        }
    }

    public void validateRefreshToken(String token) {
        try{
            validateToken(token, "refresh");
        }catch (JwtException e) {
            throw new AuthenticationException("JWT validation failed: " + e.getMessage(), e) {};
        }
    }

}

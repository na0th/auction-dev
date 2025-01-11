package com.example.na0th.auction.common.util;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseCookie;

public class CookieUtil {
    private static final String COOKIE_NAME = "refresh-token";
    private static final Integer COOKIE_AGE_SECONDS = 1209600;

    // 쿠키 생성
    public static ResponseCookie createResponseCookie(String refreshTokenValue) {
        return ResponseCookie.from(COOKIE_NAME, refreshTokenValue)
                .httpOnly(true)
                .secure(true)
                .sameSite("None") // 또는 "Lax", "Strict" 필요에 따라 선택
                .path("/")
                .maxAge(COOKIE_AGE_SECONDS)
                .build();
    }

    // 쿠키 값 가져오기
    public static String getCookieValue(HttpServletRequest request, String name) {
        if (request.getCookies() != null) {
            for (Cookie cookie : request.getCookies()) {
                if (cookie.getName().equals(name)) {
                    return cookie.getValue();
                }
            }
        }
        return null;
    }
}

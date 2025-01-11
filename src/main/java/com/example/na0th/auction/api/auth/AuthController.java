package com.example.na0th.auction.api.auth;

import com.example.na0th.auction.common.response.ApiResult;
import com.example.na0th.auction.common.util.CookieUtil;
import com.example.na0th.auction.common.util.JwtTokenProvider;
import com.example.na0th.auction.domain.auth.dto.request.AuthRequest;
import com.example.na0th.auction.domain.auth.exception.RefreshTokenNotFoundException;
import com.example.na0th.auction.domain.auth.model.RefreshToken;
import com.example.na0th.auction.domain.auth.service.AuthService;
import com.example.na0th.auction.domain.user.model.MyUserDetails;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static com.example.na0th.auction.common.constant.ApiResponseMessages.AUTH_LOGIN_SUCCESS;
import static com.example.na0th.auction.common.constant.ApiResponseMessages.AUTH_TOKEN_REISSUE_SUCCESS;
import static org.springframework.http.HttpHeaders.SET_COOKIE;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider tokenProvider;
    private final AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<?> authenticateUser(@RequestBody AuthRequest.Login loginRequest, HttpServletResponse response) {
        // 사용자 인증
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword())
        );
        // userId 추출
        MyUserDetails principal = (MyUserDetails) authentication.getPrincipal();
        Long userId = principal.getUserId();
        // 토큰 발급
        String accessToken = tokenProvider.generateAccessToken(userId);
        String refreshToken = tokenProvider.generateRefreshToken();
        // 리프레시 토큰을 DB에 저장
        authService.saveRefreshToken(refreshToken, userId);
        // access token 은 쿠키가 아닌 세션 스토리지에 저장
        // 리프레시 토큰, 클라이언트 쿠키에 저장
        ResponseCookie cookie = CookieUtil.createResponseCookie(refreshToken);// 60 * 60 * 24 * 7 : 7일

        return ResponseEntity.ok()
                .header(SET_COOKIE, cookie.toString())
                .body(ApiResult.success(AUTH_LOGIN_SUCCESS, accessToken));
    }

    //access token 재발급
    @PostMapping("/reissue")
    public ResponseEntity<?> refresh(HttpServletRequest request) {
        String refreshTokenValue = CookieUtil.getCookieValue(request, "refresh-token");

        if (refreshTokenValue == null) {
            throw new RefreshTokenNotFoundException("Refresh token not found in cookie.");
        }
        //Refresh Token 검증 후, 적절하지 않다면 예외처리
        tokenProvider.validateRefreshToken(refreshTokenValue);

        RefreshToken refreshToken = authService.getRefreshToken(refreshTokenValue);
        String newAccessToken = tokenProvider.generateAccessToken(refreshToken.getUserId());

        return ResponseEntity.ok()
                .body(ApiResult.success(AUTH_TOKEN_REISSUE_SUCCESS, newAccessToken));
    }
}

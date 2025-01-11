package com.example.na0th.auction.domain.auth.service;

import com.example.na0th.auction.domain.auth.exception.RefreshTokenNotFoundException;
import com.example.na0th.auction.domain.auth.model.RefreshToken;
import com.example.na0th.auction.domain.auth.repository.RefreshTokenRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {
    private final RefreshTokenRepository refreshTokenRepository;

    @Override
    public void saveRefreshToken(String refreshTokenValue, Long userId) {
        RefreshToken refreshToken = RefreshToken.of(refreshTokenValue, userId);
        refreshTokenRepository.save(refreshToken);
    }

    @Override
    public RefreshToken getRefreshToken(String refreshToken) {
        return refreshTokenRepository.findById(refreshToken).orElseThrow(() -> new RefreshTokenNotFoundException("Refresh Token not found with id " + refreshToken));
    }

}

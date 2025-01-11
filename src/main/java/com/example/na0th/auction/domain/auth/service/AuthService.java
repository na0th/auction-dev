package com.example.na0th.auction.domain.auth.service;

import com.example.na0th.auction.domain.auth.model.RefreshToken;

public interface AuthService {
    void saveRefreshToken(String refreshToken, Long userId);

    RefreshToken getRefreshToken(String refreshToken);
}

package com.example.na0th.auction.domain.auth.repository;

import com.example.na0th.auction.domain.auth.model.RefreshToken;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken, String> {
}

package com.example.na0th.auction.common.config;

import io.jsonwebtoken.Jwts;
import lombok.Getter;
import org.springframework.context.annotation.Configuration;

import javax.crypto.SecretKey;

@Getter
@Configuration
public class SecretKeyConfig {
    private final SecretKey secretKey;

    public SecretKeyConfig() {this.secretKey = Jwts.SIG.HS256.key().build();}
}

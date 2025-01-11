package com.example.na0th.auction.common.properties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@Getter
@Setter
@ConfigurationProperties(prefix = "jwt") // "jwt" 접두사를 가진 설정들을 바인딩
public class JwtProperties {
    private long accessTokenExpiration;
    private long refreshTokenExpiration;
}

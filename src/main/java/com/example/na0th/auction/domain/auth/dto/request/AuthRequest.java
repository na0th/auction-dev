package com.example.na0th.auction.domain.auth.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class AuthRequest {

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Login {
        private String email;
        private String password;
    }
}

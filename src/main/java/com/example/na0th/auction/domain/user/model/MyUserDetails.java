package com.example.na0th.auction.domain.user.model;

import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

@Builder
@RequiredArgsConstructor
public class MyUserDetails implements UserDetails {
    @Getter
    private final Long userId;
    private final String userEmail;
    private final String password;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of();
    }

    @Override
    public String getPassword() {
        return password;
    }

    // 사용자 ID(email) 반환
    @Override
    public String getUsername() {
        return userEmail;
    }

    public static MyUserDetails from(Long userId) {
        return MyUserDetails.builder()
                .userId(userId)
                .userEmail(null)
                .password(null)
                .build();
    }
}

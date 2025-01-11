package com.example.na0th.auction.domain.auth.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.*;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class RefreshToken {
    @Id
    private String id;
    @Column(nullable = false)
    private Long userId;

    public static RefreshToken of(String id, Long userId) {
        return RefreshToken.builder()
                .id(id)
                .userId(userId)
                .build();
    }
}

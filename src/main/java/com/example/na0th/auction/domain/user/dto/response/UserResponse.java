package com.example.na0th.auction.domain.user.dto.response;


import com.example.na0th.auction.domain.user.model.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class UserResponse {
    private Long id;
    private String nickName;
    private String name;
    private String email;

    public static UserResponse from(User user) {
        return UserResponse.builder()
                .id(user.getId())
                .nickName(user.getNickName())
                .name(user.getName())
                .email(user.getEmail())
                .build();
    }
}

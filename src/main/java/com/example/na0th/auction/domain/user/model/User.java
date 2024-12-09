package com.example.na0th.auction.domain.user.model;

import com.example.na0th.auction.common.entity.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class User extends BaseEntity {

    @Id
    @GeneratedValue
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false, unique = true) //unique 특성 추가 필요
    private String email;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private String nickName;

    @Column(nullable = true)
    private LocalDateTime deletedAt;

    //    private List<Auction> auctions = new ArrayList<>();
    //    private Wallet wallet;

    public static User create(String name, String email, String password, String nickname) {
        return User.builder()
                .name(name)
                .email(email)
                .password(password)
                .nickName(nickname)
                .build();
    }

    public void update(String name, String password, String nickName) {
        this.name = name;
        this.password = password;
        this.nickName = nickName;
    }

}

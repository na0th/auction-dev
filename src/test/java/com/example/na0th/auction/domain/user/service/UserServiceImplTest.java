package com.example.na0th.auction.domain.user.service;

import com.example.na0th.auction.domain.user.dto.request.UserRequest;
import com.example.na0th.auction.domain.user.dto.response.UserResponse;
import com.example.na0th.auction.domain.user.exception.UserNotFoundException;
import com.example.na0th.auction.domain.user.model.User;
import com.example.na0th.auction.domain.user.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {
    @Mock
    UserRepository userRepository;
    @InjectMocks
    UserServiceImpl userService;

    private User user;

    @BeforeEach
    void setUp() {
        user = User.builder()
                .id(1L)
                .name("name")
                .email("email")
                .password("password")
                .nickName("nickname")
                .build();

    }

    @Test
    void 유저를_생성한다() {
        //given
        UserRequest.Create request = new UserRequest.Create("username", "email", "password", "nickname");
        when(userRepository.save(any(User.class))).thenReturn(user);
        //when
        UserResponse createdUser = userService.create(request);
        //then
        assertThat(createdUser.getId()).isEqualTo(user.getId());
        assertThat(createdUser.getName()).isEqualTo(user.getName());
        assertThat(createdUser.getEmail()).isEqualTo(user.getEmail());
        assertThat(createdUser.getNickName()).isEqualTo(user.getNickName());

    }

    @Nested
    class 유저_조회 {
        @Test
        void 유저를_조회한다() {
            //given
            when(userRepository.findById(any(Long.class))).thenReturn(Optional.of(user));
            //when
            UserResponse foundUser = userService.getById(1L);
            //then
            assertThat(foundUser.getId()).isEqualTo(1L);
            assertThat(foundUser.getName()).isEqualTo(user.getName());
            assertThat(foundUser.getEmail()).isEqualTo(user.getEmail());
            assertThat(foundUser.getNickName()).isEqualTo(user.getNickName());

        }

        @Test
        void 유저조회_시_없는_id면_예외가_발생한다() {
            // given
            when(userRepository.findById(1L)).thenReturn(Optional.empty());
            // when & then
            assertThatThrownBy(() -> userService.getById(1L))
                    .isInstanceOf(UserNotFoundException.class);
        }

    }

    @Nested
    class 유저_수정 {
        @Test
        void 유저를_수정한다() {
            //given
            UserRequest.Update request = new UserRequest.Update("UpdatedName", "UpdatedPassword", "UpdatedNickname");
            when(userRepository.findById(any(Long.class))).thenReturn(Optional.of(user));
            //when
            UserResponse updatedUser = userService.update(1L, request);
            //then
            assertThat(updatedUser.getId()).isEqualTo(1L);
            assertThat(updatedUser.getName()).isEqualTo(request.getName());
            assertThat(updatedUser.getNickName()).isEqualTo(request.getNickname());
        }
        @Test
        void 유저를_수정_시_없는_id면_예외가_발생한다() {
            //given
            UserRequest.Update request = new UserRequest.Update("UpdatedName", "UpdatedPassword", "UpdatedNickname");
            when(userRepository.findById(any(Long.class))).thenReturn(Optional.empty());
            //when & then
            assertThatThrownBy(() -> userService.update(1L, request)).isInstanceOf(UserNotFoundException.class);
        }
    }

    @Test
    void 유저를_삭제한다() {
        //given
        when(userRepository.findById(any(Long.class))).thenReturn(Optional.of(user));
        //when
        userService.delete(1L);
        //then
        verify(userRepository).deleteById(eq(1L));
    }
}
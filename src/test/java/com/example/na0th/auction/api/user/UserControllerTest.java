package com.example.na0th.auction.api.user;

import com.example.na0th.auction.common.config.JpaAuditingConfig;
import com.example.na0th.auction.common.constant.ApiResponseMessages;
import com.example.na0th.auction.domain.user.dto.request.UserRequest;
import com.example.na0th.auction.domain.user.dto.response.UserResponse;
import com.example.na0th.auction.domain.user.exception.UserNotFoundException;
import com.example.na0th.auction.domain.user.model.User;
import com.example.na0th.auction.domain.user.service.UserService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = UserController.class, excludeAutoConfiguration = JpaAuditingConfig.class)
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private UserService userService;

    @BeforeEach
    void setUp() {

    }

    @Nested
    class 등록 {
        @Test
        void 유저를_등록한다() throws Exception {
            // given
            UserRequest.Create request = new UserRequest.Create("username", "email", "password", "nickname");
            UserResponse createdUser = new UserResponse(1L, "nickname", "username", "email");

            when(userService.create(any(UserRequest.Create.class))).thenReturn(createdUser);
            // when & then
            mockMvc.perform(post("/api/v1/users")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(request)))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.data.id").value(1L))
                    .andExpect(jsonPath("$.data.nickName").value("nickname"))
                    .andExpect(jsonPath("$.data.name").value("username"))
                    .andExpect(jsonPath("$.data.email").value("email"));
        }
    }


    @Nested
    class 조회 {
        @Test
        void 유저_조회를_성공한다() throws Exception {
            // given
            UserResponse foundUser = new UserResponse(1L, "nickname", "username", "email");
            when(userService.getById(any(Long.class))).thenReturn(foundUser);
            // when & then
            mockMvc.perform(get("/api/v1/users/{id}", 1L))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.success").value(true))
                    .andExpect(jsonPath("$.data.id").value(1L))
                    .andExpect(jsonPath("$.data.nickName").value("nickname"))
                    .andExpect(jsonPath("$.data.name").value("username"))
                    .andExpect(jsonPath("$.data.email").value("email"));

        }

        @Test
        void 유저_조회를_실패한다() throws Exception {
            // given
            when(userService.getById(anyLong())).thenThrow(new UserNotFoundException("User not found with id 1"));
            // when & then
            mockMvc.perform(get("/api/v1/users/{id}", 2L))
                    .andExpect(status().isNotFound())
                    .andExpect(jsonPath("$.success").value(false))
                    .andExpect(jsonPath("$.message").value(ApiResponseMessages.USER_NOT_FOUND));
        }
    }


    @Nested
    class 수정 {
        @Test
        void 유저_수정을_성공한다() throws Exception {
            // given
            UserRequest.Update request = new UserRequest.Update("updatedUsername", "updatedPassword", "updatedNickname");
            UserResponse updatedUser = new UserResponse(1L, "updatedNickname", "updatedUsername", "email");
            when(userService.update(any(Long.class), any(UserRequest.Update.class))).thenReturn(updatedUser);
            // when & then
            mockMvc.perform(put("/api/v1/users/{id}", 1L)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(request)))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.success").value(true))
                    .andExpect(jsonPath("$.data.id").value(1L))
                    .andExpect(jsonPath("$.data.nickName").value("updatedNickname"))
                    .andExpect(jsonPath("$.data.name").value("updatedUsername"))
                    .andExpect(jsonPath("$.data.email").value("email"));

        }

        @Test
        void 유저_수정을_실패한다() throws Exception {
            // given
            UserRequest.Update request = new UserRequest.Update("updatedUsername", "updatedPassword", "updatedNickname");
            when(userService.update(any(Long.class), any(UserRequest.Update.class))).thenThrow(UserNotFoundException.class);
            // when & then
            mockMvc.perform(put("/api/v1/users/{id}", 1L)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(request)))
                    .andExpect(status().isNotFound())
                    .andExpect(jsonPath("$.success").value(false))
                    .andExpect(jsonPath("$.message").value(ApiResponseMessages.USER_NOT_FOUND));
        }

    }

    @Nested
    class 삭제 {
        @Test
        void 유저_삭제를_성공한다() throws Exception {
            // given
            doNothing().when(userService).delete(any(Long.class));
            // when & then
            mockMvc.perform(delete("/api/v1/users/{id}", 1L))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.success").value(true))
                    .andExpect(jsonPath("$.message").value(ApiResponseMessages.USER_DELETED_SUCCESS));
        }

        @Test
        void 유저_삭제를_실패한다() throws Exception {
            // given
            doThrow(UserNotFoundException.class).when(userService).delete(any(Long.class));
            // when & then
            mockMvc.perform(delete("/api/v1/users/{id}", 1L))
                    .andExpect(status().isNotFound())
                    .andExpect(jsonPath("$.success").value(false))
                    .andExpect(jsonPath("$.message").value(ApiResponseMessages.USER_NOT_FOUND));
        }
    }

}
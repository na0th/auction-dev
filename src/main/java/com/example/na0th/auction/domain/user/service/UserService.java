package com.example.na0th.auction.domain.user.service;

import com.example.na0th.auction.domain.user.dto.request.UserRequest;
import com.example.na0th.auction.domain.user.dto.response.UserResponse;

public interface UserService {
    UserResponse create(UserRequest.Create create);

    UserResponse getById(Long userId);

    UserResponse update(Long userId, UserRequest.Update request);

    void delete(Long userId);
}

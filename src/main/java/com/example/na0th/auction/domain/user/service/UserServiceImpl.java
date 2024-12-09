package com.example.na0th.auction.domain.user.service;

import com.example.na0th.auction.domain.user.dto.request.UserRequest;
import com.example.na0th.auction.domain.user.dto.response.UserResponse;
import com.example.na0th.auction.domain.user.exception.UserNotFoundException;
import com.example.na0th.auction.domain.user.model.User;
import com.example.na0th.auction.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;

    @Override
    public UserResponse create(UserRequest.Create create) {
        User user = User.create(
                create.getUsername(),
                create.getEmail(),
                create.getPassword(),
                create.getNickname()
        );
        User newUser = userRepository.save(user);

        return UserResponse.from(newUser);
    }

    @Override
    public UserResponse getById(Long userId) {
        User foundUser = userRepository.findById(userId)
                .orElseThrow( () -> new UserNotFoundException("User not found with id " + userId));

        return UserResponse.from(foundUser);
    }

    @Override
    public UserResponse update(Long userId, UserRequest.Update request) {
        User foundUser = userRepository.findById(userId)
                .orElseThrow( () -> new UserNotFoundException("User not found with id " + userId));

        foundUser.update(
                request.getName(),
                request.getPassword(),
                request.getNickname()
        );
        return UserResponse.from(foundUser);
    }

    @Override
    public void delete(Long userId) {
        userRepository.deleteById(userId);
    }
}

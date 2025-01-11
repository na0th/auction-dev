package com.example.na0th.auction.domain.user.service;

import com.example.na0th.auction.domain.user.model.MyUserDetails;
import com.example.na0th.auction.domain.user.model.User;
import com.example.na0th.auction.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {
    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("not found : userEmail =" + email));

        return new MyUserDetails(user.getId(), user.getEmail(), user.getPassword());
    }
}

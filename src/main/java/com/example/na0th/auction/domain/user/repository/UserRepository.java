package com.example.na0th.auction.domain.user.repository;

import com.example.na0th.auction.domain.user.model.User;
import org.springframework.data.jpa.repository.JpaRepository;


public interface UserRepository extends JpaRepository<User, Long> {
}

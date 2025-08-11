package com.application.signup.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.application.signup.entity.User;

public interface UserRepository extends JpaRepository<User, Integer> {
    Optional<User> findByEmail(String email);
}
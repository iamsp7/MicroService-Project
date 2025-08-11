package com.application.signin.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.application.signin.entity.SignupEntity;



public interface SignupRepository extends JpaRepository<SignupEntity, Integer> {
    Optional<SignupEntity> findByEmail(String email);
}
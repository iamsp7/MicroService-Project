package com.application.signup.service;

import java.util.Optional;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.application.signup.dto.UserDTO;
import com.application.signup.entity.User;
import com.application.signup.repository.UserRepository;
import com.application.signup.util.EncryptionUtil;

@Service
public class SignupService {

    private static final Logger logger = LogManager.getLogger(SignupService.class);

    @Autowired
    private UserRepository userRepository;

    public String register(UserDTO dto) {
        logger.info("Attempting to register user with email: {}", dto.getEmail());

        Optional<User> existing = userRepository.findByEmail(dto.getEmail());
        if (existing.isPresent()) {
            logger.warn("Registration failed: Email {} is already registered", dto.getEmail());
            throw new RuntimeException("Email already registered");
        }

        try {
            User user = new User();
            user.setUsername(dto.getUsername());
            user.setEmail(dto.getEmail());
            user.setFirstName(dto.getFirstName());
            user.setLastName(dto.getLastName());
            user.setRole(dto.getRole());
            user.setPassword(EncryptionUtil.encrypt(dto.getPassword()));

            userRepository.save(user);
            logger.info("User {} registered successfully", dto.getEmail());

            return "User registered successfully";
        } catch (Exception e) {
            logger.error("Error occurred while registering user: {}", dto.getEmail(), e);
            throw new RuntimeException("Failed to register user");
        }
    }



    public UserDTO getUserByEmail(String email) {
        logger.info("Fetching user with email: {}", email);

        User user = userRepository.findByEmail(email)
            .orElseThrow(() -> {
                logger.warn("User with email {} not found", email);
                return new RuntimeException("User not found");
            });

        logger.debug("User found: {}", user.getEmail());

        UserDTO dto = new UserDTO();
        dto.setUsername(user.getUsername());
        dto.setEmail(user.getEmail());

        // 🔓 Decrypt the password before setting it
        String decryptedPassword = EncryptionUtil.decrypt(user.getPassword());
        dto.setPassword(decryptedPassword);

        dto.setFirstName(user.getFirstName());
        dto.setLastName(user.getLastName());
        dto.setRole(user.getRole());

        return dto;
    }

}

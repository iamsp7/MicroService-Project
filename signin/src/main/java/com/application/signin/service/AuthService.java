package com.application.signin.service;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.application.signin.dto.UserDTO;
import com.application.signin.entity.User;
import com.application.signin.repository.UserRepository;
import com.application.signin.util.EncryptionUtil;
import com.application.signin.util.JwtUtil;

@Service
public class AuthService {

    private static final Logger logger = LogManager.getLogger(AuthService.class);

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ReportService reportService;

    public String signin(UserDTO request) {
        logger.info("Signin attempt for username: {}", request.getUsername());

        User user = userRepository.findByUsername(request.getUsername())
            .orElseThrow(() -> {
                logger.warn("User not found: {}", request.getUsername());
                return new RuntimeException("User not found");
            });

        String decrypted = EncryptionUtil.decrypt(user.getPassword());

        if (!decrypted.equals(request.getPassword())) {
            logger.warn("Invalid credentials for username: {}", request.getUsername());
            throw new RuntimeException("Invalid credentials");
        }

        String token = JwtUtil.generateToken(user.getUsername(), user.getRole());

        logger.info("Token generated for user: {}", user.getUsername());

        try {
            reportService.generateAndSendReport(user);
        } catch (Exception e) {
            logger.error("Error sending report: {}", e.getMessage());
        }

        return token;
    }
}

   

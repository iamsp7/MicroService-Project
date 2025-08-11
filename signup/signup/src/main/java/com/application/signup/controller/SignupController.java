package com.application.signup.controller;


import com.application.signup.dto.UserActivityDTO;
import com.application.signup.dto.UserDTO;
import com.application.signup.service.SignupService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/signup")
public class SignupController {

    @Autowired
    private SignupService signupService;

    @PostMapping
    public ResponseEntity<String> signup(@RequestBody UserDTO dto) {
        return ResponseEntity.ok(signupService.register(dto));
    }

    @GetMapping("/user/email/{email}")
    public ResponseEntity<UserDTO> getUserByEmail(@PathVariable String email) {
        return ResponseEntity.ok(signupService.getUserByEmail(email));
    }

    // ✅ NEW ENDPOINT: Return user signup activity for report-service
    @GetMapping("/activity")
    public ResponseEntity<List<UserActivityDTO>> getSignupLogs(
            @RequestParam("startTime") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startTime,
            @RequestParam("endTime") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endTime) {

        // 🔧 Replace this mock later with real DB activity log
        return ResponseEntity.ok(
                List.of(
                        new UserActivityDTO("sahil.patil@example.com", startTime.plusHours(1)),
                        new UserActivityDTO("alex.smith@example.com", startTime.plusHours(3))
                )
        );
    }
}

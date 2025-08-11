package com.application.signin.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.application.signin.dto.SignupDTO;
import com.application.signin.service.SignupService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

@RestController
@RequestMapping("/signup")
@Tag(name = "Signup Controller", description = "Handles user registration and lookup")
public class SignupController {

    @Autowired
    private SignupService signupService;

    @Operation(
        summary = "Register a new user",
        description = "Registers a new user in the system with provided details"
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "User registered successfully"),
        @ApiResponse(responseCode = "400", description = "Invalid input data"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PostMapping
    public ResponseEntity<String> signup(@RequestBody SignupDTO dto) {
        return ResponseEntity.ok(signupService.register(dto));
    }

    @Operation(
        summary = "Get user by email",
        description = "Fetches the registered user details using email"
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "User found"),
        @ApiResponse(responseCode = "404", description = "User not found"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping("/user/email/{email}")
    public ResponseEntity<SignupDTO> getUserByEmail(@PathVariable String email) {
        return ResponseEntity.ok(signupService.getUserByEmail(email));
    }
}

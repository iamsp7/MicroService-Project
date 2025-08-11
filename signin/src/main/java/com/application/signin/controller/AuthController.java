package com.application.signin.controller;

import com.application.signin.dto.UserDTO;
import com.application.signin.entity.User;
import com.application.signin.repository.UserRepository;
import com.application.signin.service.AuthService;
import com.application.signin.service.ReportService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.*;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/signin")
@Tag(name = "Authentication Controller", description = "Handles user login and role-based access")
public class AuthController {

    @Autowired
    private AuthService authService;

    @Autowired
    private ReportService reportService;

    @Autowired
    private UserRepository userRepository;

    @Operation(summary = "Login User", description = "Logs in a user and returns a JWT token if credentials are valid")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Login successful"),
            @ApiResponse(responseCode = "401", description = "Invalid credentials")
    })
    @PostMapping
    public String login(@RequestBody UserDTO request) {
        return authService.signin(request);
    }

    @Operation(summary = "Access for USER role", description = "Accessible only by users with ROLE_USER")
    @PreAuthorize("hasRole('USER')")
    @GetMapping("/secure")
    public ResponseEntity<?> secureAccess() {
        return ResponseEntity.ok("Access granted to secured USER");
    }

    @Operation(summary = "Access for ADMIN role", description = "Accessible only by users with ROLE_ADMIN")
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/admin")
    public ResponseEntity<?> adminAccess() {
        return ResponseEntity.ok("Access granted to ADMIN-only");
    }

    @GetMapping("/success")
    public ResponseEntity<String> oauth2Success(@AuthenticationPrincipal OAuth2User principal) {
        String email = principal.getAttribute("email");
        return ResponseEntity.ok("Login successful! Email: " + email);
    }

    // 🔁 Manual report generation via POST
    @PostMapping("/report/email")
    public ResponseEntity<String> generateAndEmailReport(@RequestBody UserDTO userDto) {
        User user = userRepository.findByUsername(userDto.getUsername())
                .orElseThrow(() -> new RuntimeException("User not found"));

        try {
            reportService.generateAndSendReport(user);
            return ResponseEntity.ok("Report sent to " + user.getEmail());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Failed to send report: " + e.getMessage());
        }
    }

    // 📄 Download PDF
    @GetMapping("/report/download/pdf")
    public ResponseEntity<ByteArrayResource> downloadPdf(@RequestParam String username) {
        return getReportResponse(username, "pdf");
    }

    // 📊 Download Excel
    @GetMapping("/report/download/excel")
    public ResponseEntity<ByteArrayResource> downloadExcel(@RequestParam String username) {
        return getReportResponse(username, "excel");
    }

    // 📝 Download Word
    @GetMapping("/report/download/word")
    public ResponseEntity<ByteArrayResource> downloadWord(@RequestParam String username) {
        return getReportResponse(username, "word");
    }

    // 🔧 Internal helper method to avoid duplicate code
    private ResponseEntity<ByteArrayResource> getReportResponse(String username, String type) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        ByteArrayResource resource;
        String filename;
        MediaType mediaType;

        try {
            switch (type) {
                case "pdf" -> {
                    resource = new ByteArrayResource(reportService.generatePdf(user).toByteArray());
                    filename = "login-report.pdf";
                    mediaType = MediaType.APPLICATION_PDF;
                }
                case "excel" -> {
                    resource = new ByteArrayResource(reportService.generateExcel(user).toByteArray());
                    filename = "login-report.xlsx";
                    mediaType = MediaType.APPLICATION_OCTET_STREAM;
                }
                case "word" -> {
                    resource = new ByteArrayResource(reportService.generateWord(user).toByteArray());
                    filename = "login-report.docx";
                    mediaType = MediaType.APPLICATION_OCTET_STREAM;
                }
                default -> throw new IllegalArgumentException("Invalid report type");
            }

            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + filename)
                    .contentType(mediaType)
                    .body(resource);

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}

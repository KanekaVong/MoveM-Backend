package com.movem.backend.controller;

import com.movem.backend.dto.request.AuthRequest.*;
import com.movem.backend.entity.EmailVerification;
import com.movem.backend.entity.User;
import com.movem.backend.exception.ResourceNotFoundException;
import com.movem.backend.repository.AuthRepository.EmailVerificationRepository;
import com.movem.backend.service.AuthServices.EmailService;
import com.movem.backend.service.AuthServices.JwtService;
import com.movem.backend.service.AuthServices.OtpService;
import com.movem.backend.service.AuthServices.UserService;
import com.movem.backend.service.AuthServices.CurrentUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired private UserService userService;
    @Autowired private AuthenticationManager authenticationManager;
    @Autowired private JwtService jwtService;
    @Autowired private OtpService otpService;
    @Autowired private EmailService emailService;
    @Autowired private EmailVerificationRepository emailVerificationRepository;
    @Autowired private PasswordEncoder passwordEncoder;
    @Autowired private CurrentUserService currentUserService;


    @PostMapping("/register")
    public ResponseEntity<String> register(
            @RequestBody RegisterRequest request
    ) {
        userService.registerUser(request);
        return ResponseEntity.ok("User registered successfully!");
    }

    @PostMapping("/verify-email")
    public ResponseEntity<?> verifyEmail(@RequestBody EmailVerifyRequest request) {
        User user = userService.getUserByEmail(request.getEmail());

        EmailVerification verification = emailVerificationRepository
                .findTopByUserIdOrderByCreatedAtDesc(user.getId())
                .orElseThrow(() -> new ResourceNotFoundException("No verification code found. Please register again."));

        if (verification.getUsedAt() != null) {
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("error", "This code has already been used.");
            return ResponseEntity.status(400).body(errorResponse);
        }
        if (verification.getExpiresAt().isBefore(LocalDateTime.now())) {
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("error", "Code expired. Please request a new one.");
            return ResponseEntity.status(400).body(errorResponse);
        }
        if (!passwordEncoder.matches(request.getCode(), verification.getCodeHash())) {
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("error", "Invalid code.");
            return ResponseEntity.status(401).body(errorResponse);
        }

        verification.setUsedAt(LocalDateTime.now());
        emailVerificationRepository.save(verification);

        user.setIsActive(true);
        userService.updateUser(user);

        String accessToken = jwtService.generateToken(user.getUsername(), user.getPasswordChangedAt());
        String trustToken = jwtService.generateTrustToken(user.getUsername(), user.getPasswordChangedAt());

        Map<String, String> response = new HashMap<>();
        response.put("message", "Email verified successfully!");
        response.put("accessToken", accessToken);
        response.put("trustToken", trustToken);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request) {
        Authentication auth = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword())
        );
        String username = auth.getName();
        User user = userService.getUserByUsername(username);

        if (request.getTrustToken() != null && !request.getTrustToken().isEmpty()) {
            try {
                String tokenUsername = jwtService.extractUsername(request.getTrustToken());
                String tokenPwdChangedAt = jwtService.extractPasswordChangedAtClaim(request.getTrustToken());
                String currentPwdChangedAt = user.getPasswordChangedAt() != null
                        ? user.getPasswordChangedAt().toString() : "null";

                if (jwtService.isTrustToken(request.getTrustToken())
                        && tokenUsername.equals(username)
                        && tokenPwdChangedAt.equals(currentPwdChangedAt)) {

                    String accessToken = jwtService.generateToken(username, user.getPasswordChangedAt());
                    String newTrustToken = jwtService.generateTrustToken(username, user.getPasswordChangedAt());

                    Map<String, String> response = new HashMap<>();
                    response.put("accessToken", accessToken);
                    response.put("trustToken", newTrustToken);
                    return ResponseEntity.ok(response);
                }
            } catch (Exception e) {
                // fall through to OTP
            }
        }

        String otpCode = otpService.generateOtp(username);
        emailService.sendOtpEmail(user.getEmail(), otpCode);
        return ResponseEntity.ok("OTP sent to your email.");
    }

    @PostMapping("/verify-otp")
    public ResponseEntity<?> verifyOtp(@RequestBody OtpVerifyRequest request) {
        boolean isValid = otpService.verifyOtp(request.getUsername(), request.getOtp());
        if (!isValid) {
            return ResponseEntity.status(401).body("Invalid or expired OTP.");
        }

        User user = userService.getUserByUsername(request.getUsername());
        String accessToken = jwtService.generateToken(user.getUsername(), user.getPasswordChangedAt());
        String trustToken = jwtService.generateTrustToken(user.getUsername(), user.getPasswordChangedAt());

        Map<String, String> response = new HashMap<>();
        response.put("accessToken", accessToken);
        response.put("trustToken", trustToken);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/forgot-password")
    public ResponseEntity<String> forgotPassword(@RequestBody ForgotPasswordRequest request) {
        userService.findByEmailOptional(request.getEmail()).ifPresent(user -> {
            String otpCode = otpService.generateOtp(user.getUsername());
            emailService.sendPasswordResetEmail(user.getEmail(), otpCode);
        });

        return ResponseEntity.ok("If that email exists, a reset code has been sent.");
    }

    @PostMapping("/reset-password")
    public ResponseEntity<?> resetPassword(@RequestBody ResetPasswordRequest request) {
        User user = userService.getUserByEmail(request.getEmail());

        // Check the new password rule FIRST — before touching the OTP at all
        if (passwordEncoder.matches(request.getNewPassword(), user.getPasswordHash())) {
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("error", "New password must be different from your current password.");
            return ResponseEntity.status(400).body(errorResponse);
        }

        // Only now verify (and consume) the OTP
        boolean isValid = otpService.verifyOtp(user.getUsername(), request.getOtp());
        if (!isValid) {
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("error", "Invalid or expired code.");
            return ResponseEntity.status(401).body(errorResponse);
        }

        userService.updatePassword(request.getEmail(), request.getNewPassword());

        User updatedUser = userService.getUserByEmail(request.getEmail());
        String accessToken = jwtService.generateToken(updatedUser.getUsername(), updatedUser.getPasswordChangedAt());
        String trustToken = jwtService.generateTrustToken(updatedUser.getUsername(), updatedUser.getPasswordChangedAt());

        Map<String, String> response = new HashMap<>();
        response.put("message", "Password reset successfully.");
        response.put("accessToken", accessToken);
        response.put("trustToken", trustToken);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/me")
    public ResponseEntity<String> me() {

        User user = currentUserService.getCurrentUser();

        return ResponseEntity.ok(user.getUsername());

    }
}
package com.movem.backend.Controller.AuthController;


import com.movem.backend.Dto.request.AuthRequest.*;
import com.movem.backend.Dto.response.AuthResponses.AuthResponse;
import com.movem.backend.Dto.response.AuthResponses.CurrentUserResponse;
import com.movem.backend.Entity.Auth.EmailVerification;
import com.movem.backend.Entity.Auth.TrustedDevice;
import com.movem.backend.Entity.Auth.User;
import com.movem.backend.Exception.ResourceNotFoundException;
import com.movem.backend.Mapper.BaseMapper.CurrentUserMapper;
import com.movem.backend.Repository.AuthRepository.EmailVerificationRepository;
import com.movem.backend.Repository.AuthRepository.TrustedDeviceRepository;
import com.movem.backend.Service.AuthServices.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import com.movem.backend.Exception.EmailNotVerifiedException;
import org.springframework.security.authentication.DisabledException;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired private UserService userService;
    @Autowired private AuthenticationManager authenticationManager;
    @Autowired private com.movem.backend.service.AuthServices.JwtService jwtService;
    @Autowired private OtpService otpService;
    @Autowired private EmailService emailService;
    @Autowired private EmailVerificationRepository emailVerificationRepository;
    @Autowired private PasswordEncoder passwordEncoder;
    @Autowired private CurrentUserService currentUserService;
    @Autowired private TrustedDeviceRepository trustedDeviceRepository;
    @Autowired private CurrentUserMapper currentUserMapper;

    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody User user) {
        userService.registerUser(user);
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
        com.movem.backend.service.AuthServices.JwtService.TrustTokenResult trustResult =
                jwtService.generateTrustToken(
                        user.getUsername(),
                        user.getPasswordChangedAt(),
                        request.getDeviceId()
                );

        String trustToken = trustResult.token();
        String jti = trustResult.jti();

        TrustedDevice trustedDevice = TrustedDevice.builder()
                .user(user)
                .deviceId(request.getDeviceId())
                .jti(jti)
                .createdAt(LocalDateTime.now())
                .expiresAt(LocalDateTime.now().plusDays(3))
                .build();

        trustedDeviceRepository.save(trustedDevice);

        AuthResponse response = AuthResponse.builder()
                .accessToken(accessToken)
                .trustToken(trustToken)
                .user(buildCurrentUserResponse(user))
                .build();

        return ResponseEntity.ok(response);
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request) {
        Authentication auth;
        try {
            auth = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword())
            );
        } catch (DisabledException e) {
            User unverifiedUser = userService.findByUsernameOrEmail(request.getUsername());

            if (!passwordEncoder.matches(
                    request.getPassword(),
                    unverifiedUser.getPasswordHash()
            )) {
                throw new BadCredentialsException(
                        "Invalid username or password."
                );
            }
            // Send a fresh EMAIL VERIFICATION code
            userService.sendVerificationCode(unverifiedUser);

            // Tell Flutter to open the email verification screen
            throw new EmailNotVerifiedException(
                    unverifiedUser.getEmail()
            );
        }

        String username = auth.getName();
        User user = userService.getUserByUsername(username);

        if (request.getTrustToken() != null && !request.getTrustToken().isEmpty()) {
            try {
                String tokenUsername = jwtService.extractUsername(request.getTrustToken());
                String tokenPwdChangedAt = jwtService.extractPasswordChangedAtClaim(request.getTrustToken());
                String currentPwdChangedAt = user.getPasswordChangedAt() != null
                        ? user.getPasswordChangedAt().toString() : "null";
                String tokenDeviceId = jwtService.extractDeviceId(request.getTrustToken());
                String tokenJti = jwtService.extractJti(request.getTrustToken());

                Optional<TrustedDevice> trustedDevice =
                        trustedDeviceRepository.findByJti(tokenJti);

                if (jwtService.isTrustToken(request.getTrustToken())
                        && tokenUsername.equals(username)
                        && tokenPwdChangedAt.equals(currentPwdChangedAt)
                        && tokenDeviceId.equals(request.getDeviceId())
                        && trustedDevice.isPresent()
                        && trustedDevice.get().getRevokedAt() == null
                        && trustedDevice.get().getDeviceId().equals(request.getDeviceId())
                        && trustedDevice.get().getExpiresAt().isAfter(LocalDateTime.now())
                        && trustedDevice.get().getUser().getId().equals(user.getId())) {

                    String accessToken = jwtService.generateToken(username, user.getPasswordChangedAt());
                    com.movem.backend.service.AuthServices.JwtService.TrustTokenResult trustResult =
                            jwtService.generateTrustToken(
                                    username,
                                    user.getPasswordChangedAt(),
                                    request.getDeviceId()
                            );

                    String newTrustToken = trustResult.token();
                    String jti = trustResult.jti();

                    trustedDevice.get().setRevokedAt(LocalDateTime.now());
                    trustedDeviceRepository.save(trustedDevice.get());

                    TrustedDevice newTrustedDevice  = TrustedDevice.builder()
                            .user(user)
                            .deviceId(request.getDeviceId())
                            .jti(jti)
                            .createdAt(LocalDateTime.now())
                            .expiresAt(LocalDateTime.now().plusDays(3))
                            .build();

                    trustedDeviceRepository.save(newTrustedDevice );

                    AuthResponse response = AuthResponse.builder()
                            .accessToken(accessToken)
                            .trustToken(newTrustToken)
                            .user(buildCurrentUserResponse(user))
                            .build();

                    return ResponseEntity.ok(response);
                }
            } catch (Exception e) {
                // fall through to OTP
            }
        }

        String otpCode = otpService.generateOtp(username);
        emailService.sendOtpEmail(user.getEmail(), otpCode);
        AuthResponse response = AuthResponse.builder()
                .message("OTP sent to your email.")
                .build();
        return ResponseEntity.ok(response);
    }

    @PostMapping("/verify-otp")
    public ResponseEntity<?> verifyOtp(@RequestBody OtpVerifyRequest request) {
        User user = userService.findByUsernameOrEmail(request.getUsername());

        boolean isValid = otpService.verifyOtp(
                user.getUsername(),
                request.getOtp()
        );
        if (!isValid) {
            return ResponseEntity.status(401).body("Invalid or expired OTP.");
        }

        String accessToken = jwtService.generateToken(user.getUsername(), user.getPasswordChangedAt());
        com.movem.backend.service.AuthServices.JwtService.TrustTokenResult trustResult =
                jwtService.generateTrustToken(
                        user.getUsername(),
                        user.getPasswordChangedAt(),
                        request.getDeviceId()
                );

        String trustToken = trustResult.token();
        String jti = trustResult.jti();

        Optional<TrustedDevice> existingDevice =
                trustedDeviceRepository.findByUserIdAndDeviceIdAndRevokedAtIsNull(
                        user.getId(),
                        request.getDeviceId()
                );

        if (existingDevice.isPresent()) {
            existingDevice.get().setRevokedAt(LocalDateTime.now());
            trustedDeviceRepository.save(existingDevice.get());
        }

        TrustedDevice trustedDevice = TrustedDevice.builder()
                .user(user)
                .deviceId(request.getDeviceId())
                .jti(jti)
                .createdAt(LocalDateTime.now())
                .expiresAt(LocalDateTime.now().plusDays(3))
                .build();

        trustedDeviceRepository.save(trustedDevice);

        AuthResponse response = AuthResponse.builder()
                .accessToken(accessToken)
                .trustToken(trustToken)
                .user(buildCurrentUserResponse(user))
                .build();

        return ResponseEntity.ok(response);
    }

    @PostMapping("/resend-login-otp")
    public ResponseEntity<Map<String, String>> resendLoginOtp(@RequestBody ResendOtpRequest request) {
        User user = userService.getUserByUsername(request.getUsername());

        String otpCode = otpService.generateOtp(user.getUsername());
        emailService.sendOtpEmail(user.getEmail(), otpCode);

        return ResponseEntity.ok(Map.of("message", "A new code has been sent to your email."));
    }

    @PostMapping("/resend-verification")
    public ResponseEntity<Map<String, String>> resendVerification(
            @RequestBody Map<String, String> request) {

        userService.resendVerificationCode(request.get("email"));

        return ResponseEntity.ok(
                Map.of("message", "A new verification code has been sent to your email.")
        );
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
        com.movem.backend.service.AuthServices.JwtService.TrustTokenResult trustResult =
                jwtService.generateTrustToken(
                        updatedUser.getUsername(),
                        updatedUser.getPasswordChangedAt(),
                        request.getDeviceId()
                );

        String trustToken = trustResult.token();
        String jti = trustResult.jti();

        Optional<TrustedDevice> existingDevice =
                trustedDeviceRepository.findByUserIdAndDeviceIdAndRevokedAtIsNull(
                        updatedUser.getId(),
                        request.getDeviceId()
                );

        if (existingDevice.isPresent()) {
            existingDevice.get().setRevokedAt(LocalDateTime.now());
            trustedDeviceRepository.save(existingDevice.get());
        }

        TrustedDevice trustedDevice = TrustedDevice.builder()
                .user(updatedUser)
                .deviceId(request.getDeviceId())
                .jti(jti)
                .createdAt(LocalDateTime.now())
                .expiresAt(LocalDateTime.now().plusDays(3))
                .build();

        trustedDeviceRepository.save(trustedDevice);
        AuthResponse response = AuthResponse.builder()
                .accessToken(accessToken)
                .trustToken(trustToken)
                .user(buildCurrentUserResponse(updatedUser))
                .build();

        return ResponseEntity.ok(response);
    }

    private CurrentUserResponse buildCurrentUserResponse(User user) {
        return CurrentUserResponse.builder()
                .id(user.getId())
                .username(user.getUsername())
                .email(user.getEmail())
                .firstName(user.getFirstname())
                .lastName(user.getLastname())
                .dateOfBirth(user.getDateOfBirth())
                .jointDate(user.getJointDate())
                .profilePic(user.getProfilePic())
                .cityProvince(user.getCityProvince())
                .isActive(user.getIsActive())
                .themePreference(user.getThemePreference())
                .languagePreference(user.getLanguagePreference())
                .build();
    }

    @GetMapping("/me")
    public ResponseEntity<CurrentUserResponse> me() {

        User user =
                currentUserService.getCurrentUser();

        return ResponseEntity.ok(
                currentUserMapper.toResponse(user)
        );
    }
}

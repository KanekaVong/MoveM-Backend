package com.movem.backend.Controller.SharedController;

import com.movem.backend.Dto.request.AuthRequest.ChangeEmailRequest;
import com.movem.backend.Dto.request.AuthRequest.ChangePasswordRequest;
import com.movem.backend.Dto.request.AuthRequest.UpdateProfileRequest;
import com.movem.backend.Dto.response.AuthResponses.AuthResponse;
import com.movem.backend.Dto.response.AuthResponses.UserResponse;
import com.movem.backend.Entity.Auth.TrustedDevice;
import com.movem.backend.Entity.Auth.User;
import com.movem.backend.Mapper.BaseMapper.CurrentUserMapper;
import com.movem.backend.Repository.AuthRepository.TrustedDeviceRepository;
import com.movem.backend.Service.AuthServices.CurrentUserService;
import com.movem.backend.Service.AuthServices.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.*;
import com.movem.backend.Dto.request.AuthRequest.VerifyPhoneRequest;
import com.movem.backend.Service.AuthServices.JwtService;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final CurrentUserService currentUserService;
    private final UserService userService;
    private final CurrentUserMapper currentUserMapper;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final TrustedDeviceRepository trustedDeviceRepository;


    @GetMapping("/me")
    public ResponseEntity<UserResponse> getCurrentUser() {

        User user = currentUserService.getCurrentUser();

        return ResponseEntity.ok(
                currentUserMapper.toResponse(user)
        );
    }

    @PatchMapping("/me")
    public ResponseEntity<UserResponse> updateProfile(
            @Valid @RequestBody UpdateProfileRequest request
    ) {

        User currentUser = currentUserService.getCurrentUser();

        User updatedUser = userService.updateProfile(
                currentUser,
                request
        );

        return ResponseEntity.ok(
                currentUserMapper.toResponse(updatedUser)
        );
    }

    @PatchMapping("/me/profile-picture")
    public ResponseEntity<UserResponse> updateProfilePicture(
            @RequestBody Map<String, String> request
    ) {
        User currentUser = currentUserService.getCurrentUser();

        String profilePic = request.get("profilePic");

        currentUser.setProfilePic(
                profilePic == null || profilePic.isBlank()
                        ? null
                        : profilePic.trim()
        );

        userService.updateUser(currentUser);

        return ResponseEntity.ok(
                currentUserMapper.toResponse(currentUser)
        );
    }

    @PatchMapping("/me/unlink-phone")
    public ResponseEntity<UserResponse> unlinkPhone() {

        User currentUser = currentUserService.getCurrentUser();

        User updatedUser = userService.unlinkPhone(currentUser);

        return ResponseEntity.ok(
                currentUserMapper.toResponse(updatedUser)
        );
    }

    @PostMapping("/me/change-email")
    public ResponseEntity<Map<String, Object>> requestEmailChange(
            @Valid @RequestBody ChangeEmailRequest request
    ) {
        User currentUser = currentUserService.getCurrentUser();

        userService.requestEmailChange(
                currentUser,
                request.getEmail()
        );

        return ResponseEntity.ok(
                Map.of(
                        "message", "Verification code sent to your new email address."
                )
        );
    }

    @PostMapping("/me/verify-email-change")
    public ResponseEntity<UserResponse> verifyEmailChange(
            @RequestBody Map<String, String> request
    ) {
        User currentUser = currentUserService.getCurrentUser();

        String code = request.get("code");

        if (code == null || code.isBlank()) {
            throw new IllegalArgumentException(
                    "Verification code is required."
            );
        }

        User updatedUser = userService.verifyEmailChange(
                currentUser,
                code
        );

        return ResponseEntity.ok(
                currentUserMapper.toResponse(updatedUser)
        );
    }


    @PostMapping("/me/resend-email-change")
    public ResponseEntity<Map<String, String>> resendEmailChangeCode() {
        User currentUser = currentUserService.getCurrentUser();

        userService.resendEmailChangeCode(currentUser);

        return ResponseEntity.ok(
                Map.of(
                        "message",
                        "A new verification code has been sent to your email address."
                )
        );
    }

    @PostMapping("/me/verify-phone")
    public ResponseEntity<UserResponse> verifyPhone(
            @Valid @RequestBody VerifyPhoneRequest request
    ) {
        User currentUser = currentUserService.getCurrentUser();

        User updatedUser = userService.verifyPhone(
                currentUser,
                request.getFirebaseIdToken()
        );

        return ResponseEntity.ok(
                currentUserMapper.toResponse(updatedUser)
        );
    }

    @PatchMapping("/me/change-password")
    public ResponseEntity<?> changePassword(
            @Valid @RequestBody ChangePasswordRequest request
    ) {
        User currentUser = currentUserService.getCurrentUser();

        // Check current password
        if (!passwordEncoder.matches(
                request.getCurrentPassword(),
                currentUser.getPasswordHash()
        )) {
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put(
                    "error",
                    "Current password is incorrect."
            );

            return ResponseEntity.status(401).body(errorResponse);
        }

        // New password must be different
        if (passwordEncoder.matches(
                request.getNewPassword(),
                currentUser.getPasswordHash()
        )) {
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put(
                    "error",
                    "New password must be different from your current password."
            );

            return ResponseEntity.status(400).body(errorResponse);
        }

        // Update password + passwordChangedAt
        userService.updatePassword(
                currentUser.getEmail(),
                request.getNewPassword()
        );

        User updatedUser =
                userService.getUserByEmail(currentUser.getEmail());

        // Generate NEW access token
        String accessToken =
                jwtService.generateToken(
                        updatedUser.getUsername(),
                        updatedUser.getPasswordChangedAt()
                );

        // Generate NEW trust token
        JwtService.TrustTokenResult trustResult =
                jwtService.generateTrustToken(
                        updatedUser.getUsername(),
                        updatedUser.getPasswordChangedAt(),
                        request.getDeviceId()
                );

        String trustToken = trustResult.token();
        String jti = trustResult.jti();

        // Revoke existing trusted device entry
        Optional<TrustedDevice> existingDevice =
                trustedDeviceRepository
                        .findByUserIdAndDeviceIdAndRevokedAtIsNull(
                                updatedUser.getId(),
                                request.getDeviceId()
                        );

        if (existingDevice.isPresent()) {
            existingDevice.get().setRevokedAt(
                    LocalDateTime.now()
            );

            trustedDeviceRepository.save(
                    existingDevice.get()
            );
        }

        // Save new trusted device entry
        TrustedDevice trustedDevice =
                TrustedDevice.builder()
                        .user(updatedUser)
                        .deviceId(request.getDeviceId())
                        .jti(jti)
                        .createdAt(LocalDateTime.now())
                        .expiresAt(
                                LocalDateTime.now().plusDays(3)
                        )
                        .build();

        trustedDeviceRepository.save(trustedDevice);

        AuthResponse response =
                AuthResponse.builder()
                        .accessToken(accessToken)
                        .trustToken(trustToken)
                        .user(
                                currentUserMapper.toResponse(
                                        updatedUser
                                )
                        )
                        .build();

        return ResponseEntity.ok(response);
    }

}
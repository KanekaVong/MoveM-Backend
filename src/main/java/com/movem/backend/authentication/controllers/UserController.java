package com.movem.backend.authentication.controllers;

import com.movem.backend.authentication.dtos.requests.ChangeEmailRequest;
import com.movem.backend.authentication.dtos.requests.ChangePasswordRequest;
import com.movem.backend.authentication.dtos.requests.UpdateProfileRequest;
import com.movem.backend.authentication.dtos.requests.VerifyPhoneRequest;
import com.movem.backend.authentication.dtos.responses.AuthResponse;
import com.movem.backend.authentication.dtos.responses.UserProfileResponse;
import com.movem.backend.authentication.dtos.responses.UserResponse;
import com.movem.backend.authentication.dtos.responses.UserSummaryResponse;
import com.movem.backend.authentication.entities.TrustedDevice;
import com.movem.backend.authentication.entities.User;
import com.movem.backend.authentication.mappers.CurrentUserMapper;
import com.movem.backend.authentication.repositories.TrustedDeviceRepository;
import com.movem.backend.authentication.services.CurrentUserService;
import com.movem.backend.authentication.services.JwtService;
import com.movem.backend.authentication.services.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/user")
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
        return ResponseEntity.ok(currentUserMapper.toResponse(user));
    }

    @GetMapping
    public ResponseEntity<List<UserSummaryResponse>> getAllUsers() {
        List<UserSummaryResponse> users = userService.getAllUsers().stream().map(this::toSummaryResponse).toList();
        return ResponseEntity.ok(users);
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserProfileResponse> getUserById(@PathVariable Integer id) {
        User user = userService.getUserById(id);
        List<User> friends = userService.getFriends(id);
        List<UserSummaryResponse> friendResponses = friends.stream().map(this::toSummaryResponse).toList();

        UserProfileResponse response = UserProfileResponse.builder()
                        .id(user.getId())
                        .username(user.getUsername())
                        .firstname(user.getFirstname())
                        .lastname(user.getLastname())
                        .dateOfBirth(user.getDateOfBirth())
                        .jointDate(user.getJointDate())
                        .phone(user.getPhone())
                        .bio(user.getBio())
                        .gender(user.getGender())
                        .profilePic(user.getProfilePic())
                        .cityProvince(user.getCityProvince())
                        .isActive(user.getIsActive())
                        .friendsCount(friendResponses.size())
                        .friends(friendResponses)
                        .build();

        return ResponseEntity.ok(response);
    }


    @PatchMapping("/me")
    public ResponseEntity<UserResponse> updateProfile(@Valid @RequestBody UpdateProfileRequest request) {
        User currentUser = currentUserService.getCurrentUser();
        User updatedUser = userService.updateProfile(currentUser, request);

        return ResponseEntity.ok(currentUserMapper.toResponse(updatedUser));
    }

    @PatchMapping("/me/profile-picture")
    public ResponseEntity<UserResponse> updateProfilePicture(@RequestBody Map<String, String> request) {

        User currentUser = currentUserService.getCurrentUser();
        String profilePic = request.get("profilePic");

        currentUser.setProfilePic(profilePic == null || profilePic.isBlank() ? null : profilePic.trim());

        userService.updateUser(currentUser);

        return ResponseEntity.ok(currentUserMapper.toResponse(currentUser));
    }

    @PatchMapping("/me/unlink-phone")
    public ResponseEntity<UserResponse> unlinkPhone() {
        User currentUser = currentUserService.getCurrentUser();
        User updatedUser = userService.unlinkPhone(currentUser);

        return ResponseEntity.ok(currentUserMapper.toResponse(updatedUser));
    }

    @PostMapping("/me/change-email")
    public ResponseEntity<Map<String, Object>> requestEmailChange(@Valid @RequestBody ChangeEmailRequest request) {
        User currentUser = currentUserService.getCurrentUser();
        userService.requestEmailChange(currentUser, request.getEmail());
        return ResponseEntity.ok(Map.of("message", "Verification code sent to your new email address."));
    }

    @PostMapping("/me/verify-email-change")
    public ResponseEntity<UserResponse> verifyEmailChange(@RequestBody Map<String, String> request) {
        User currentUser = currentUserService.getCurrentUser();
        String code = request.get("code");
        if (code == null || code.isBlank()) {
            throw new IllegalArgumentException("Verification code is required.");
        }

        User updatedUser = userService.verifyEmailChange(currentUser, code);
        return ResponseEntity.ok(currentUserMapper.toResponse(updatedUser));
    }

    @PostMapping("/me/resend-email-change")
    public ResponseEntity<Map<String, String>> resendEmailChangeCode() {
        User currentUser = currentUserService.getCurrentUser();
        userService.resendEmailChangeCode(currentUser);

        return ResponseEntity.ok(Map.of("message", "A new verification code has been sent to your email address."));
    }

    @PostMapping("/me/verify-phone")
    public ResponseEntity<UserResponse> verifyPhone(@Valid @RequestBody VerifyPhoneRequest request) {

        User currentUser = currentUserService.getCurrentUser();
        User updatedUser = userService.verifyPhone(currentUser, request.getFirebaseIdToken());

        return ResponseEntity.ok(currentUserMapper.toResponse(updatedUser));
    }

    @PatchMapping("/me/change-password")
    public ResponseEntity<?> changePassword(@Valid @RequestBody ChangePasswordRequest request) {

        User currentUser = currentUserService.getCurrentUser();

        if (!passwordEncoder.matches(request.getCurrentPassword(), currentUser.getPasswordHash())) {
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("error", "Current password is incorrect.");

            return ResponseEntity.status(401).body(errorResponse);
        }

        if (passwordEncoder.matches(request.getNewPassword(), currentUser.getPasswordHash())) {
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("error", "New password must be different from your current password.");

            return ResponseEntity.status(400).body(errorResponse);
        }
        userService.updatePassword(currentUser.getEmail(), request.getNewPassword());

        User updatedUser = userService.getUserByEmail(currentUser.getEmail());

        String accessToken = jwtService.generateToken(updatedUser.getUsername(), updatedUser.getPasswordChangedAt());

        JwtService.TrustTokenResult trustResult = jwtService.generateTrustToken(updatedUser.getUsername(), updatedUser.getPasswordChangedAt(), request.getDeviceId());

        String trustToken = trustResult.token();
        String jti = trustResult.jti();

        Optional<TrustedDevice> existingDevice = trustedDeviceRepository.findByUserIdAndDeviceIdAndRevokedAtIsNull(updatedUser.getId(), request.getDeviceId());

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
                        .user(currentUserMapper.toResponse(updatedUser))
                        .build();

        return ResponseEntity.ok(response);
    }

    private UserSummaryResponse toSummaryResponse(User user) {
        return UserSummaryResponse.builder()
                .id(user.getId())
                .username(user.getUsername())
                .firstname(user.getFirstname())
                .lastname(user.getLastname())
                .profilePic(user.getProfilePic())
                .bio(user.getBio())
                .build();
    }

}
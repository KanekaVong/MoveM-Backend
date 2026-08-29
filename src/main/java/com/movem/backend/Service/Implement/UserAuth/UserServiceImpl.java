package com.movem.backend.Service.Implement.UserAuth;


import com.movem.backend.Entity.Auth.EmailVerification;
import com.movem.backend.Entity.Auth.User;
import com.movem.backend.Exception.DuplicateResourceException;
import com.movem.backend.Exception.ResourceNotFoundException;
import com.movem.backend.Repository.AuthRepository.EmailVerificationRepository;
import com.movem.backend.Repository.AuthRepository.UserRepository;
import com.movem.backend.Service.AuthServices.EmailService;
import com.movem.backend.Service.AuthServices.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import com.movem.backend.Dto.request.AuthRequest.UpdateProfileRequest;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.FirebaseToken;
import com.google.firebase.auth.UserRecord;

import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private EmailVerificationRepository emailVerificationRepository;

    @Autowired
    private EmailService emailService;

    private final SecureRandom secureRandom = new SecureRandom();

    @Override
    public User registerUser(User user){
        if (userRepository.findByUsername(user.getUsername()).isPresent()) {
            throw new DuplicateResourceException("Username already exists.");
        }
        if (userRepository.findByEmail(user.getEmail()).isPresent()) {
            throw new DuplicateResourceException("Email already exists.");
        }

        String hashedPassword = passwordEncoder.encode(user.getPasswordHash());
        user.setPasswordHash(hashedPassword);
        user.setIsActive(false);
        user.setPasswordChangedAt(LocalDateTime.now());

        User savedUser = userRepository.save(user);

        // Reuse the same email-verification logic
        sendVerificationCode(savedUser);

        return savedUser;
    }

    @Override
    public void resendVerificationCode(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("No account found with this email."));

        if (Boolean.TRUE.equals(user.getIsActive())) {
            throw new IllegalArgumentException("This account is already verified. Please log in.");
        }

        sendVerificationCode(user);
    }

    // Shared helper — used by both registerUser() and resendVerificationCode()
    @Override
    public void sendVerificationCode(User user) {
        String code = String.format("%06d", secureRandom.nextInt(1_000_000));

        EmailVerification verification = EmailVerification.builder()
                .userId(user.getId())
                .codeHash(passwordEncoder.encode(code))
                .expiresAt(LocalDateTime.now().plusMinutes(15))
                .createdAt(LocalDateTime.now())
                .build();

        emailVerificationRepository.save(verification);

        emailService.sendEmailVerification(
                user.getEmail(),
                code
        );
    }

    @Override
    public User findByUsernameOrEmail(String usernameOrEmail) {
        return userRepository.findByUsernameOrEmail(usernameOrEmail, usernameOrEmail)
                .orElseThrow(() -> new ResourceNotFoundException("User not found: " + usernameOrEmail));
    }

    @Override
    public User getUserByUsername(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("User not found: " + username));
    }

    @Override
    public void updatePassword(String email, String newRawPassword) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with email: " + email));

        user.setPasswordHash(passwordEncoder.encode(newRawPassword));
        user.setPasswordChangedAt(LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS)); // truncate
        userRepository.save(user);
    }

    @Override
    public User getUserByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with email: " + email));
    }

    @Override
    public User updateProfile(User user, UpdateProfileRequest request) {
        // First name
        if (request.getFirstname() != null) {
            user.setFirstname(
                    request.getFirstname().isBlank()
                            ? null
                            : request.getFirstname().trim()
            );
        }
        // Last name
        if (request.getLastname() != null) {
            user.setLastname(
                    request.getLastname().isBlank()
                            ? null
                            : request.getLastname().trim()
            );
        }
        // Bio
        if (request.getBio() != null) {
            user.setBio(
                    request.getBio().isBlank()
                            ? null
                            : request.getBio().trim()
            );
        }
        // City / Province
        if (request.getCityProvince() != null) {
            user.setCityProvince(
                    request.getCityProvince().isBlank()
                            ? null
                            : request.getCityProvince().trim()
            );
        }
        // Username
        if (request.getUsername() != null) {

            if (request.getUsername().isBlank()) {
                throw new IllegalArgumentException(
                        "Username cannot be empty."
                );
            }

            String newUsername = request.getUsername().trim();

            if (!newUsername.equals(user.getUsername())) {
                userRepository.findByUsername(newUsername)
                        .ifPresent(existingUser -> {
                            if (!existingUser.getId().equals(user.getId())) {
                                throw new DuplicateResourceException(
                                        "Username already exists."
                                );
                            }
                        });

                user.setUsername(newUsername);
            }
        }
        // Gender
        if (request.getGender() != null) {

            if (request.getGender().isBlank()) {
                user.setGender(null);
            } else {
                try {
                    user.setGender(
                            com.movem.backend.model.enums.Auth.Gender.valueOf(
                                    request.getGender().trim().toUpperCase()
                            )
                    );
                } catch (IllegalArgumentException e) {
                    throw new IllegalArgumentException(
                            "Invalid gender value."
                    );
                }
            }
        }
        // Date of birth
        if (request.getDateOfBirth() != null) {

            if (request.getDateOfBirth().isBlank()) {
                user.setDateOfBirth(null);
            } else {
                try {
                    user.setDateOfBirth(
                            java.time.LocalDate.parse(
                                    request.getDateOfBirth().trim()
                            )
                    );
                } catch (java.time.format.DateTimeParseException e) {
                    throw new IllegalArgumentException(
                            "Invalid date of birth. Use yyyy-MM-dd."
                    );
                }
            }
        }

        return userRepository.save(user);
    }

    @Override
    public void updateUser(User user) {
        userRepository.save(user);
    }

    @Override
    public User unlinkPhone(User user) {
        user.setPhone(null);
        return userRepository.save(user);
    }

    @Override
    public void requestEmailChange(User user, String newEmail) {
        String normalizedEmail = newEmail.trim().toLowerCase();

        if (normalizedEmail.equalsIgnoreCase(user.getEmail())) {
            throw new IllegalArgumentException(
                    "New email must be different from your current email."
            );
        }

        if (userRepository.findByEmail(normalizedEmail).isPresent()) {
            throw new DuplicateResourceException(
                    "Email is already in use."
            );
        }

        String code = String.format(
                "%06d",
                new java.util.Random().nextInt(1_000_000)
        );

        EmailVerification verification = EmailVerification.builder()
                .userId(user.getId())
                .pendingEmail(normalizedEmail)
                .codeHash(passwordEncoder.encode(code))
                .expiresAt(LocalDateTime.now().plusMinutes(5))
                .createdAt(LocalDateTime.now())
                .build();

        emailVerificationRepository.save(verification);

        emailService.sendEmailVerification(
                normalizedEmail,
                code
        );
    }

    @Override
    public User  verifyEmailChange(User user, String code) {
        EmailVerification verification = emailVerificationRepository
                .findTopByUserIdOrderByCreatedAtDesc(user.getId())
                .orElseThrow(() ->
                        new IllegalArgumentException(
                                "Verification code not found."
                        )
                );

        if (verification.getUsedAt() != null) {
            throw new IllegalArgumentException(
                    "Verification code has already been used."
            );
        }

        if (verification.getExpiresAt().isBefore(LocalDateTime.now())) {
            throw new IllegalArgumentException(
                    "Verification code has expired."
            );
        }

        if (verification.getPendingEmail() == null) {
            throw new IllegalArgumentException(
                    "No email change is pending."
            );
        }

        if (!passwordEncoder.matches(
                code,
                verification.getCodeHash()
        )) {
            throw new IllegalArgumentException(
                    "Invalid verification code."
            );
        }

        user.setEmail(verification.getPendingEmail());
        userRepository.save(user);

        verification.setUsedAt(LocalDateTime.now());
        emailVerificationRepository.save(verification);

        return user;
    }

    @Override
    public void resendEmailChangeCode(User user) {
        EmailVerification latestVerification =
                emailVerificationRepository
                        .findTopByUserIdOrderByCreatedAtDesc(user.getId())
                        .orElseThrow(() ->
                                new IllegalArgumentException(
                                        "No email change is pending."
                                )
                        );

        if (latestVerification.getPendingEmail() == null) {
            throw new IllegalArgumentException(
                    "No email change is pending."
            );
        }

        String pendingEmail = latestVerification.getPendingEmail();

        String code = String.format(
                "%06d",
                new java.security.SecureRandom().nextInt(1_000_000)
        );

        EmailVerification verification = EmailVerification.builder()
                .userId(user.getId())
                .pendingEmail(pendingEmail)
                .codeHash(passwordEncoder.encode(code))
                .expiresAt(LocalDateTime.now().plusMinutes(5))
                .createdAt(LocalDateTime.now())
                .build();

        emailVerificationRepository.save(verification);

        emailService.sendEmailVerification(
                pendingEmail,
                code
        );
    }

    @Override
    public User verifyPhone(User user, String firebaseIdToken) {
        if (firebaseIdToken == null || firebaseIdToken.isBlank()) {
            throw new IllegalArgumentException(
                    "Firebase ID token is required."
            );
        }

        try {
            FirebaseToken decodedToken =
                    FirebaseAuth.getInstance().verifyIdToken(firebaseIdToken);

            String firebaseUid = decodedToken.getUid();

            UserRecord firebaseUser =
                    FirebaseAuth.getInstance().getUser(firebaseUid);

            String verifiedPhone = firebaseUser.getPhoneNumber();

            if (verifiedPhone == null || verifiedPhone.isBlank()) {
                throw new IllegalArgumentException(
                        "No verified phone number was found."
                );
            }

            user.setPhone(verifiedPhone);

            return userRepository.save(user);

        } catch (FirebaseAuthException e) {
            throw new IllegalArgumentException(
                    "Invalid Firebase ID token."
            );
        }
    }

    @Override
    public Optional<User> findByEmailOptional(String email) {
        return userRepository.findByEmail(email);
    }
}
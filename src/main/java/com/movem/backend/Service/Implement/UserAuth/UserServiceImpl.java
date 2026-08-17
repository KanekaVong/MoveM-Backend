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
    public void updateUser(User user) {
        userRepository.save(user);
    }

    @Override
    public Optional<User> findByEmailOptional(String email) {
        return userRepository.findByEmail(email);
    }
}
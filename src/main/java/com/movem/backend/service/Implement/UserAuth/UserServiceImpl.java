package com.movem.backend.service.Implement.UserAuth;

import com.movem.backend.dto.request.AuthRequest.RegisterRequest;
import com.movem.backend.entity.EmailVerification;
import com.movem.backend.entity.User;
import com.movem.backend.exception.DuplicateResourceException;
import com.movem.backend.exception.ResourceNotFoundException;
import com.movem.backend.repository.AuthRepository.EmailVerificationRepository;
import com.movem.backend.repository.AuthRepository.UserRepository;
import com.movem.backend.service.AuthServices.EmailService;
import com.movem.backend.service.AuthServices.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Optional;
import java.util.Random;

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

    @Override
    public User registerUser(RegisterRequest request) {

        if (userRepository.findByUsername(request.getUsername()).isPresent()) {
            throw new DuplicateResourceException("Username already exists.");
        }

        if (userRepository.findByEmail(request.getEmail()).isPresent()) {
            throw new DuplicateResourceException("Email already exists.");
        }

        User user = new User();

        user.setUsername(request.getUsername());
        user.setEmail(request.getEmail());

        // If your DTO still uses passwordHash
        user.setPasswordHash(
                passwordEncoder.encode(request.getPassword())
        );

        // If you rename it to password later, use:
        // user.setPasswordHash(passwordEncoder.encode(request.getPassword()));

        user.setIsActive(false);
        user.setPasswordChangedAt(LocalDateTime.now());

        User savedUser = userRepository.save(user);

        // Generate a 6-digit code
        String code = String.format("%06d", new Random().nextInt(999999));

        EmailVerification verification = EmailVerification.builder()
                .userId(savedUser.getId())
                .codeHash(passwordEncoder.encode(code))
                .expiresAt(LocalDateTime.now().plusMinutes(15))
                .createdAt(LocalDateTime.now())
                .build();

        emailVerificationRepository.save(verification);

        emailService.sendEmailVerification(savedUser.getEmail(), code);

        return savedUser;
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
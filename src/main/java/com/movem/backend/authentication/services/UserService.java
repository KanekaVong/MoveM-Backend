package com.movem.backend.authentication.services;


import com.movem.backend.authentication.dtos.requests.UpdateProfileRequest;
import com.movem.backend.authentication.entities.User;

import java.util.List;
import java.util.Optional;

public interface UserService {
    User registerUser(User user);
    User getUserByUsername(String username);
    User getUserByEmail(String email);
    void sendVerificationCode(User user);
    Optional<User> findByEmailOptional(String email);
    void resendVerificationCode(String email);
    User findByUsernameOrEmail(String usernameOrEmail);
    User updateProfile(User user, UpdateProfileRequest request);
    void updateUser(User user);
    User unlinkPhone(User user);
    void updatePassword(String email, String newRawPassword);
    void requestEmailChange(User user, String newEmail);
    User verifyEmailChange(User user, String code);
    void resendEmailChangeCode(User user);
    User verifyPhone(User user, String firebaseIdToken);
    List<User> getAllUsers();
    User getUserById(Integer id);
    List<User> getFriends(Integer userId);
}
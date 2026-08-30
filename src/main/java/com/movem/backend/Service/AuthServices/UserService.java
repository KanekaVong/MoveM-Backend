package com.movem.backend.Service.AuthServices;


import com.movem.backend.Dto.request.AuthRequest.UpdateProfileRequest;
import com.movem.backend.Entity.Auth.User;

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
}
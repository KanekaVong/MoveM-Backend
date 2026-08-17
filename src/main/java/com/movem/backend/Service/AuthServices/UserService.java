package com.movem.backend.Service.AuthServices;


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
    void updateUser(User user);
    void updatePassword(String email, String newRawPassword);

}
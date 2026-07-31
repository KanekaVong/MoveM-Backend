package com.movem.backend.service.AuthServices;

import com.movem.backend.dto.request.AuthRequest.RegisterRequest;
import com.movem.backend.entity.User;

import java.util.Optional;

public interface UserService {
    User registerUser(RegisterRequest request);
    User getUserByUsername(String username);
    User getUserByEmail(String email);
    Optional<User> findByEmailOptional(String email);
    void updateUser(User user);
    void updatePassword(String email, String newRawPassword);
}

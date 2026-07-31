package com.movem.backend.dto.request.AuthRequest;

import lombok.Data;

@Data
public class LoginRequest {
    private String username;
    private String password;
    private String trustToken;
}

package com.movem.backend.Dto.request.AuthRequest;

import lombok.Data;

@Data
public class LoginRequest {
    private String username;
    private String password;
    private String trustToken;
    private String deviceId;
}
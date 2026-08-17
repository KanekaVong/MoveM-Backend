package com.movem.backend.Dto.request.AuthRequest;

import lombok.Data;

@Data
public class ResetPasswordRequest {
    private String email;
    private String otp;
    private String newPassword;
    private String deviceId;
}
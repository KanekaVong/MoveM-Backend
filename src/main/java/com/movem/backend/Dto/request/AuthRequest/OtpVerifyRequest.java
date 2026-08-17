package com.movem.backend.Dto.request.AuthRequest;

import lombok.Data;

@Data
public class OtpVerifyRequest {
    private String username;
    private String otp;
    private String deviceId;
}
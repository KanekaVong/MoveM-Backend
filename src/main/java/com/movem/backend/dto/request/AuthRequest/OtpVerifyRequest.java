package com.movem.backend.dto.request.AuthRequest;

import lombok.Data;

@Data
public class OtpVerifyRequest {
    private String username;
    private String otp;
}
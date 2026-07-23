package com.movem.backend.dto.request;

import lombok.Data;

@Data
public class OtpVerifyRequest {
    private String username;
    private String otp;
}
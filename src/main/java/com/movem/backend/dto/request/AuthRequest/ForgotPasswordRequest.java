package com.movem.backend.dto.request.AuthRequest;

import lombok.Data;

@Data
public class ForgotPasswordRequest {
    private String email;
}
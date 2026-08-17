package com.movem.backend.Dto.request.AuthRequest;

import lombok.Data;

@Data
public class ForgotPasswordRequest {
    private String email;
}
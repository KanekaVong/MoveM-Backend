package com.movem.backend.dto.request.AuthRequest;

import lombok.Data;

@Data
public class EmailVerifyRequest {
    private String email;
    private String code;
}

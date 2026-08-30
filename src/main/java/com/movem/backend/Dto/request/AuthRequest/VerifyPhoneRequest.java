package com.movem.backend.Dto.request.AuthRequest;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class VerifyPhoneRequest {

    @NotBlank
    private String firebaseIdToken;
}
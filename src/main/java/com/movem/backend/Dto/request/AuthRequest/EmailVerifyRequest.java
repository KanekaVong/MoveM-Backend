package com.movem.backend.Dto.request.AuthRequest;

import lombok.Data;

@Data
public class EmailVerifyRequest {
    private String email;
    private String code;
    private String deviceId;

}

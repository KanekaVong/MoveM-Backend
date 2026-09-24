package com.movem.backend.authentication.dtos.requests;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class OtpVerifyRequest {
     String username;
     String otp;
     String deviceId;
}
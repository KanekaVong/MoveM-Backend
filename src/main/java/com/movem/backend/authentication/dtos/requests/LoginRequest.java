package com.movem.backend.authentication.dtos.requests;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class LoginRequest {
     String username;
     String password;
     String trustToken;
     String deviceId;
}
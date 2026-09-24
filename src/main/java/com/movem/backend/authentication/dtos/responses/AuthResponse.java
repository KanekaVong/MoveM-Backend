package com.movem.backend.authentication.dtos.responses;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class AuthResponse {
     String accessToken;
     String trustToken;
     UserResponse user;
     String message;
}
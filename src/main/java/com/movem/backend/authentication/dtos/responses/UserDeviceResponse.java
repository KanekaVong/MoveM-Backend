package com.movem.backend.authentication.dtos.responses;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;

@Getter
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserDeviceResponse {
     Long id;
     Integer userId;
     String platform;
     Boolean isActive;
     LocalDateTime lastSeenAt;
}
package com.movem.backend.authentication.dtos.requests;

import jakarta.validation.constraints.NotBlank;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)

public class RegisterDeviceRequest {

    @NotBlank(message = "Device token is required.")
    String deviceToken;
    String platform;
}
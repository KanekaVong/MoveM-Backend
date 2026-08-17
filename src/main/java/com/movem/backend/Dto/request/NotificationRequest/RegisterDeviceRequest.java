package com.movem.backend.Dto.request.NotificationRequest;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RegisterDeviceRequest {

    @NotBlank(message = "Device token is required.")
    private String deviceToken;

    private String platform;
}
package com.movem.backend.authentication.dtos.requests;

import jakarta.validation.constraints.NotBlank;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import javax.lang.model.element.Name;
import java.util.logging.Level;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ChangePasswordRequest {
    @NotBlank
    String currentPassword;
    @NotBlank
    String newPassword;
    @NotBlank
    String deviceId;
}
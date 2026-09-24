package com.movem.backend.authentication.dtos.requests;

import jakarta.validation.constraints.Size;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UpdateProfileRequest {
    @Size(max = 50)
    String firstname;
    @Size(max = 50)
    String lastname;
    @Size(min = 3, max = 50)
    String username;
    @Size(max = 500)
    String bio;
    String phone;
    String gender;
    String dateOfBirth;
    @Size(max = 50)
    String cityProvince;
}
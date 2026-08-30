package com.movem.backend.Dto.request.AuthRequest;

import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UpdateProfileRequest {

    @Size(max = 50)
    private String firstname;

    @Size(max = 50)
    private String lastname;

    @Size(min = 3, max = 50)
    private String username;

    @Size(max = 500)
    private String bio;

    private String gender;

    private String dateOfBirth;

    @Size(max = 50)
    private String cityProvince;
}
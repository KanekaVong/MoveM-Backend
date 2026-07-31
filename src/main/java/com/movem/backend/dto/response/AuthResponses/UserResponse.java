package com.movem.backend.dto.response.AuthResponses;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserResponse {
    private Long id;
    private String username;
    private String email;
    private String firstname;
    private String lastname;
    private String phone;
    private String cityProvince;
    private LocalDate dateOfBirth;
    private LocalDate joinDate;
    private String languagePreference;
    private String themePreference;
    private String profilePic;
}
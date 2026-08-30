package com.movem.backend.Dto.response.AuthResponses;

import com.movem.backend.model.enums.Audit.ThemePreference;
import com.movem.backend.model.enums.Auth.Gender;
import com.movem.backend.model.enums.Auth.LanguagePreference;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Builder;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserResponse {
    private Integer id;
    private String username;
    private String email;
    private String firstname;
    private String lastname;
    private String bio;
    private String phone;
    private String cityProvince;
    private LocalDate dateOfBirth;
    private LocalDateTime jointDate;
    private ThemePreference themePreference;
    private LanguagePreference languagePreference;
    private String profilePic;
    private Gender gender;
    private Boolean isActive;

}
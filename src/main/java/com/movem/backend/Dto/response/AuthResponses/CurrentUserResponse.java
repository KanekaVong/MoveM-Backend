package com.movem.backend.Dto.response.AuthResponses;

import com.movem.backend.model.enums.Auth.Gender;
import com.movem.backend.model.enums.Auth.LanguagePreference;
import com.movem.backend.model.enums.Audit.ThemePreference;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Builder
public class CurrentUserResponse {

    private Integer id;

    private String username;

    private String email;

    private String firstName;

    private String lastName;

    private LocalDate dateOfBirth;

    private LocalDateTime jointDate;

    private byte[] profilePic;

    private String cityProvince;

    private Boolean isActive;

    private ThemePreference themePreference;

    private LanguagePreference languagePreference;

    private Gender gender;
}
package com.movem.backend.authentication.dtos.responses;

import com.movem.backend.commons.enums.Auth.ThemePreference;
import com.movem.backend.commons.enums.Auth.Gender;
import com.movem.backend.commons.enums.Auth.LanguagePreference;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserResponse {
     Integer id;
     String username;
     String email;
     String firstname;
     String lastname;
     String bio;
     String phone;
     String cityProvince;
     LocalDate dateOfBirth;
     LocalDateTime jointDate;
     ThemePreference themePreference;
     LanguagePreference languagePreference;
     String profilePic;
     Gender gender;
     Boolean isActive;

}
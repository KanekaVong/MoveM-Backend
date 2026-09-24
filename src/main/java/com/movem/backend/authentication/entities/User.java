package com.movem.backend.authentication.entities;

import com.movem.backend.commons.enums.Auth.Gender;
import com.movem.backend.commons.enums.Auth.LanguagePreference;
import com.movem.backend.commons.enums.Auth.ThemePreference;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "user")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)

public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Integer id;

    @Column(nullable = false, unique = true, length = 50)
    String username;

    @Column(nullable = false, unique = true, length = 100)
    String email;

    @Column(length = 50)
    String firstname;

    @Column(length = 50)
    String lastname;

    LocalDate dateOfBirth;

    LocalDateTime jointDate;

    @Column(length = 15)
    String phone;

    @Column(length = 500)
    String bio;

    @Enumerated(EnumType.STRING)
    @Column(columnDefinition = "ENUM('MALE','FEMALE','OTHER','PREFER_NOT_TO_SAY')")
    Gender gender;

    @Column(name = "profile_pic", length = 1000)
    String profilePic;

    @Column(name = "password_hash", nullable = false)
    String passwordHash;

    @Column(name = "password_changed_at", columnDefinition = "DATETIME(6)")
    LocalDateTime passwordChangedAt;

    @Column(length = 50)
    String cityProvince;

    @Column(name = "is_active")
    Boolean isActive = false;

    @Enumerated(EnumType.STRING)
    ThemePreference themePreference;

    @Enumerated(EnumType.STRING)
    LanguagePreference languagePreference;
}
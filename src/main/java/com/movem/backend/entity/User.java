package com.movem.backend.entity;

import com.movem.backend.model.enums.LanguagePreference;
import com.movem.backend.model.enums.ThemePreference;
import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "user")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false, unique = true, length = 50)
    private String username;

    @Column(nullable = false, unique = true, length = 100)
    private String email;

    @Column(length = 50)
    private String firstname;

    @Column(length = 50)
    private String lastname;

    private LocalDate dateOfBirth;

    private LocalDateTime jointDate;

    @Column(length = 15)
    private String phone;

    @Lob
    private byte[] profilePic; // images as byte arrays or use a URL string

    @Column(name = "password_hash", nullable = false)
    private String passwordHash;

    @Column(name = "password_changed_at")
    private LocalDateTime passwordChangedAt;

    @Column(length = 50)
    private String cityProvince;

    @Column(name = "is_active")
    private Boolean isActive = false;

    @Enumerated(EnumType.STRING)
    private ThemePreference themePreference;

    @Enumerated(EnumType.STRING)
    private LanguagePreference languagePreference;
}

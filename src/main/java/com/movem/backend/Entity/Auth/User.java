package com.movem.backend.Entity.Auth;

import com.movem.backend.model.enums.Auth.Gender;
import com.movem.backend.model.enums.Auth.LanguagePreference;
import com.movem.backend.model.enums.Audit.ThemePreference;
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

    @Enumerated(EnumType.STRING)
    @Column(
            columnDefinition = "ENUM('male','female','other','prefer_not_to_say')"
    )
    private Gender gender;

    @Lob
    private byte[] profilePic;

    @Column(name = "password_hash", nullable = false)
    private String passwordHash;

    @Column(name = "password_changed_at", columnDefinition = "DATETIME(6)")
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
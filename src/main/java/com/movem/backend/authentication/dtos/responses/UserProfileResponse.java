package com.movem.backend.authentication.dtos.responses;

import com.movem.backend.commons.enums.Auth.Gender;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserProfileResponse {

     Integer id;
     String username;
     String firstname;
     String lastname;

     LocalDate dateOfBirth;
     LocalDateTime jointDate;

     String phone;
     String bio;
     Gender gender;
     String profilePic;
     String cityProvince;

     Boolean isActive;

     Integer friendsCount;
     List<UserSummaryResponse> friends;
}
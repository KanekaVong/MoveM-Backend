package com.movem.backend.authentication.dtos.responses;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserSummaryResponse {
     Integer id;
     String username;
     String firstname;
     String lastname;
     String profilePic;
     String bio;
}

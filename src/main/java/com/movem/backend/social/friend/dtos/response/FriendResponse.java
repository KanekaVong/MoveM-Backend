package com.movem.backend.social.friend.dtos.response;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class FriendResponse {
     Integer userId;
     String username;
     String firstname;
     String lastname;
     String profilePic;
}

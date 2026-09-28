package com.movem.backend.social.friend.dtos.response;

import com.movem.backend.commons.enums.Friend.FriendStatus;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class SearchUserResponse {
     Integer userId;
     String username;
     String firstname;
     String lastname;
     String profilePic;
     FriendStatus friendStatus;
     boolean alreadyAdded;
}

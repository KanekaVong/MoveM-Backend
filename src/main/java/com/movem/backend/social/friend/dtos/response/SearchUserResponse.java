package com.movem.backend.social.friend.dtos.response;

import com.movem.backend.commons.enums.Friend.FriendStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SearchUserResponse {
    private Integer userId;
    private String username;
    private String firstname;
    private String lastname;
    private String profilePic;
    private FriendStatus friendStatus;
    private boolean alreadyAdded;
}

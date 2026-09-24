package com.movem.backend.social.friend.dtos.response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FriendResponse {

    private Integer userId;

    private String username;

    private String firstname;

    private String lastname;

    private String profilePic;

}

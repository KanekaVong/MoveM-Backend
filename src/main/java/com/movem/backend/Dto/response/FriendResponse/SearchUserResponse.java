package com.movem.backend.Dto.response.FriendResponse;

import com.movem.backend.model.enums.Friend.FriendStatus;
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

}

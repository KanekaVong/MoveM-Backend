package com.movem.backend.Dto.response.GroupAndCollabResponse;

import com.movem.backend.model.enums.Collaboration.GroupRole;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class GroupMemberResponse {

    private Integer userId;

    private String username;

    private String firstname;

    private String lastname;

    private String profilePic;

    private GroupRole role;

    private LocalDateTime joinedAt;

}
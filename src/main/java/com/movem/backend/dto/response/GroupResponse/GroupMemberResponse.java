package com.movem.backend.dto.response.GroupResponse;

import com.movem.backend.model.enums.Group.GroupRole;
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

    private byte[] profilePic;

    private GroupRole role;

    private LocalDateTime joinedAt;

}
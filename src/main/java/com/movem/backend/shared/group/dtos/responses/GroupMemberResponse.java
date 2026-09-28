package com.movem.backend.shared.group.dtos.responses;

import com.movem.backend.commons.enums.shared.GroupRole;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;

@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class GroupMemberResponse {
     Integer userId;
     String username;
     String firstname;
     String lastname;
     String profilePic;
     GroupRole role;
     LocalDateTime joinedAt;
}
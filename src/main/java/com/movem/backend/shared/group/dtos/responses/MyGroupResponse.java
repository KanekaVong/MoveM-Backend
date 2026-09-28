package com.movem.backend.shared.group.dtos.responses;

import com.movem.backend.commons.enums.shared.GroupRole;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class MyGroupResponse {
     Integer groupId;
     String activityId;
     String activityName;
     String activityDescription;
     LocalDateTime createdAt;
     Integer memberCount;
     GroupRole role;
}

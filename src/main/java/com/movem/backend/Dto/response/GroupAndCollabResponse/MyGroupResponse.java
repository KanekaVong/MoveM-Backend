package com.movem.backend.Dto.response.GroupAndCollabResponse;

import com.movem.backend.model.enums.Collaboration.GroupRole;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class MyGroupResponse {

    private Integer groupId;

    private String activityId;

    private String activityName;

    private String activityDescription;

    private LocalDateTime createdAt;

    private Integer memberCount;

    private GroupRole role;

}

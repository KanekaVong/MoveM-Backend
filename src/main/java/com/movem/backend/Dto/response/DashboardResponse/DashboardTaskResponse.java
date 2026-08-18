package com.movem.backend.Dto.response.DashboardResponse;

import com.movem.backend.model.enums.Activity.ActivityStatus;
import com.movem.backend.model.enums.Priority;
import lombok.*;

import java.time.LocalDateTime;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DashboardTaskResponse {

    private String activityId;

    private String activityName;

    private Priority priority;

    private ActivityStatus status;

    private LocalDateTime deadline;

    private Boolean isCollaborative;

}

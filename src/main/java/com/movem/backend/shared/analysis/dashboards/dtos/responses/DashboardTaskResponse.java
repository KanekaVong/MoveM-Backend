package com.movem.backend.shared.analysis.dashboards.dtos.responses;

import com.movem.backend.commons.enums.shared.ActivityStatus;
import com.movem.backend.commons.enums.Task.Priority;
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

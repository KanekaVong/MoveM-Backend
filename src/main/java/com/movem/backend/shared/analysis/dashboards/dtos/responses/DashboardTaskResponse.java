package com.movem.backend.shared.analysis.dashboards.dtos.responses;

import com.movem.backend.commons.enums.shared.ActivityStatus;
import com.movem.backend.commons.enums.Task.Priority;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class DashboardTaskResponse {
     String activityId;
     String activityName;
     Priority priority;
     ActivityStatus status;
     LocalDateTime deadline;
     Boolean isCollaborative;

}

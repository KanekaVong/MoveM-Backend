package com.movem.backend.shared.analysis.statistics.dtos.responses;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class TaskStatisticsResponse {
     long activeTasks;
     long completedTasks;
     long pendingTasks;
     long inProgressTasks;
     long overdueTasks;

     double completionRate;

     long tasksDueToday;
     long tasksDueThisWeek;
     long completedThisWeek;

     long highPriorityTasks;
     long mediumPriorityTasks;
     long lowPriorityTasks;
     long personalTasks;
     long collaborativeTasks;

}

package com.movem.backend.dto.response.StatisticsResponse;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class TaskStatisticsResponse {

    private long activeTasks;

    private long completedTasks;

    private long pendingTasks;

    private long inProgressTasks;

    private long overdueTasks;

    private double completionRate;

    private long tasksDueToday;

    private long tasksDueThisWeek;

    private long completedThisWeek;

    private long highPriorityTasks;

    private long mediumPriorityTasks;

    private long lowPriorityTasks;

    private long personalTasks;

    private long collaborativeTasks;

}

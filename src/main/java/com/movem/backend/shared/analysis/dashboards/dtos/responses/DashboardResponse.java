package com.movem.backend.shared.analysis.dashboards.dtos.responses;

import com.movem.backend.shared.historyandlogs.activityfeed.dtos.responses.ActivityFeedResponse;
import com.movem.backend.shared.analysis.statistics.dtos.responses.FitnessStatisticsResponse;
import com.movem.backend.shared.analysis.statistics.dtos.responses.TaskStatisticsResponse;
import com.movem.backend.shared.reminder.dtos.responses.ReminderResponse;
import lombok.*;

import java.util.List;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DashboardResponse {

    private TaskStatisticsResponse statistics;
    private FitnessStatisticsResponse fitnessStatistics;

    private List<DashboardTaskResponse> dueToday;

    private List<DashboardTaskResponse> overdueTasks;

    private List<DashboardTaskResponse> upcomingTasks;

    private List<ActivityFeedResponse> recentActivities;

    private List<ReminderResponse> upcomingReminders;



}
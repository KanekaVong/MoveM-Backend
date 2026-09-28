package com.movem.backend.shared.analysis.dashboards.dtos.responses;

import com.movem.backend.shared.historyandlogs.activityfeed.dtos.responses.ActivityFeedResponse;
import com.movem.backend.shared.analysis.statistics.dtos.responses.FitnessStatisticsResponse;
import com.movem.backend.shared.analysis.statistics.dtos.responses.TaskStatisticsResponse;
import com.movem.backend.shared.reminder.dtos.responses.ReminderResponse;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class DashboardResponse {
     TaskStatisticsResponse statistics;
     FitnessStatisticsResponse fitnessStatistics;
     List<DashboardTaskResponse> dueToday;
     List<DashboardTaskResponse> overdueTasks;
     List<DashboardTaskResponse> upcomingTasks;
     List<ActivityFeedResponse> recentActivities;
     List<ReminderResponse> upcomingReminders;
}
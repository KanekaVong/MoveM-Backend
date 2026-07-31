package com.movem.backend.dto.response.Dashboard;

import com.movem.backend.dto.response.ActivityFeedResponse;
import com.movem.backend.dto.response.StatisticsResponse.TaskStatisticsResponse;
import com.movem.backend.dto.response.TaskResponses.TaskReminderResponse;
import com.movem.backend.service.Implement.DashboardServices.DashboardTaskResponse;
import lombok.*;

import java.util.List;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DashboardResponse {

    private TaskStatisticsResponse statistics;

    private List<DashboardTaskResponse> dueToday;

    private List<DashboardTaskResponse> overdueTasks;

    private List<DashboardTaskResponse> upcomingTasks;

    private List<ActivityFeedResponse> recentActivities;

    private List<TaskReminderResponse> upcomingReminders;

}
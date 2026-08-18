package com.movem.backend.Dto.response.DashboardResponse;

import com.movem.backend.Dto.response.ActivityFeedResponse;
import com.movem.backend.Dto.response.StatisticsResponse.FitnessStatisticsResponse;
import com.movem.backend.Dto.response.StatisticsResponse.TaskStatisticsResponse;
import com.movem.backend.Dto.response.TaskResponses.TaskReminderResponse;
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

    private List<TaskReminderResponse> upcomingReminders;



}
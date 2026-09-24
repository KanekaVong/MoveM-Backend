package com.movem.backend.shared.analysis.dashboards.services.impl;

import com.movem.backend.shared.analysis.dashboards.dtos.responses.DashboardResponse;
import com.movem.backend.shared.analysis.dashboards.dtos.responses.DashboardTaskResponse;
import com.movem.backend.shared.analysis.statistics.services.FitnessStatisticsService;
import com.movem.backend.commons.Specification.TaskSpecification;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import com.movem.backend.task.entities.Task;
import com.movem.backend.authentication.entities.User;
import com.movem.backend.task.repositories.TaskRepository;
import com.movem.backend.authentication.services.CurrentUserService;
import com.movem.backend.shared.analysis.dashboards.services.DashboardService;
import com.movem.backend.shared.analysis.statistics.services.TaskStatisticsService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class DashboardServiceImpl
        implements DashboardService {

    private final TaskStatisticsService taskStatisticsService;
    private final FitnessStatisticsService fitnessStatisticsService;
    private final CurrentUserService currentUserService;
    private final TaskRepository taskRepository;

    @Override
    public DashboardResponse getMyDashboard() {

        User currentUser = currentUserService.getCurrentUser();

        LocalDateTime now = LocalDateTime.now();

        LocalDateTime startToday =
                now.toLocalDate().atStartOfDay();

        LocalDateTime endToday =
                startToday.plusDays(1);


        List<Task> dueTodayEntities =
                taskRepository.findAll(
                        TaskSpecification
                                .belongsToUser(currentUser)
                                .and(TaskSpecification.dueToday(
                                        startToday,
                                        endToday
                                )),
                        PageRequest.of(
                                0,
                                5,
                                Sort.by(
                                        Sort.Direction.ASC,
                                        "activity.deadline"
                                )
                        )
                ).getContent();


        List<DashboardTaskResponse> dueToday =
                dueTodayEntities.stream()
                        .map(this::mapToDashboardTask)
                        .toList();

        List<Task> overdueTaskEntities =
                taskRepository.findAll(
                        TaskSpecification
                                .belongsToUser(currentUser)
                                .and(TaskSpecification.isOverdue()),
                        PageRequest.of(
                                0,
                                5,
                                Sort.by(
                                        Sort.Direction.ASC,
                                        "activity.deadline"
                                )
                        )
                ).getContent();


        List<DashboardTaskResponse> overdueTasks =
                overdueTaskEntities.stream()
                        .map(this::mapToDashboardTask)
                        .toList();

        List<Task> upcomingTaskEntities =
                taskRepository.findAll(
                        TaskSpecification
                                .belongsToUser(currentUser)
                                .and(TaskSpecification.upcoming(7)),
                        PageRequest.of(
                                0,
                                5,
                                Sort.by(
                                        Sort.Direction.ASC,
                                        "activity.deadline"
                                )
                        )
                ).getContent();

        List<DashboardTaskResponse> upcomingTasks =
                upcomingTaskEntities.stream()
                        .map(this::mapToDashboardTask)
                        .toList();

        return DashboardResponse.builder()
                .statistics(taskStatisticsService.getMyTaskStatistics())
                .fitnessStatistics(fitnessStatisticsService.getMyFitnessStatistics())
                .dueToday(dueToday)
                .overdueTasks(overdueTasks)
                .upcomingTasks(upcomingTasks)
                .build();
    }


    private DashboardTaskResponse mapToDashboardTask(Task task) {

        return DashboardTaskResponse.builder()
                .activityId(task.getActivity().getId())
                .activityName(task.getActivity().getActivityName())
                .priority(task.getPriority())
                .status(task.getActivity().getStatus())
                .deadline(task.getActivity().getDeadline())
                .isCollaborative(task.getActivity().getIsCollaborative())
                .build();

    }

}

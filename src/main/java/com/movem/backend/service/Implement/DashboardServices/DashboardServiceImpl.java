package com.movem.backend.service.Implement.DashboardServices;

import com.movem.backend.dto.response.Dashboard.DashboardResponse;
import com.movem.backend.specification.TaskSpecification;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import com.movem.backend.entity.Tasks.Task;
import com.movem.backend.entity.User;
import com.movem.backend.repository.TaskRepositories.TaskRepository;
import com.movem.backend.service.AuthServices.CurrentUserService;
import com.movem.backend.service.DashboardServices.DashboardService;
import com.movem.backend.service.StatisticsServices.TaskStatisticsService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class DashboardServiceImpl
        implements DashboardService {

    private final TaskStatisticsService taskStatisticsService;
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

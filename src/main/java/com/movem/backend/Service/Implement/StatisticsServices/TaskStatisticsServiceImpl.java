package com.movem.backend.Service.Implement.StatisticsServices;

import com.movem.backend.Dto.response.StatisticsResponse.TaskStatisticsResponse;
import com.movem.backend.Entity.Auth.User;
import com.movem.backend.model.enums.Activity.ActivityStatus;
import com.movem.backend.model.enums.Priority;
import com.movem.backend.Repository.SharedRepository.ActivityRepository;
import com.movem.backend.Repository.TaskRepositories.TaskRepository;
import com.movem.backend.Service.AuthServices.CurrentUserService;
import com.movem.backend.Service.StatisticsServices.TaskStatisticsService;
import com.movem.backend.Specification.TaskSpecification;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class TaskStatisticsServiceImpl
        implements TaskStatisticsService {

    private final TaskRepository taskRepository;

    private final CurrentUserService currentUserService;

    private final ActivityRepository activityRepository;

    @Override
    public TaskStatisticsResponse getMyTaskStatistics() {

        User currentUser = currentUserService.getCurrentUser();


        long activeTasks =
                taskRepository.count(
                        TaskSpecification
                                .belongsToUser(currentUser)
                                .and(TaskSpecification.notDeleted())
                );

        long completedTasks =
                taskRepository.count(
                        TaskSpecification
                                .belongsToUser(currentUser)
                                .and(
                                        TaskSpecification.statusEquals(
                                                ActivityStatus.COMPLETE
                                        )
                                )
                );

        long inProgressTasks =
                taskRepository.count(
                        TaskSpecification
                                .belongsToUser(currentUser)
                                .and(
                                        TaskSpecification.statusEquals(
                                                ActivityStatus.IN_PROGRESS
                                        )
                                )
                );

        long pendingTasks =
                taskRepository.count(
                        TaskSpecification
                                .belongsToUser(currentUser)
                                .and(
                                        TaskSpecification.statusEquals(
                                                ActivityStatus.PENDING
                                        )
                                )
                );

        long overdueTasks =
                taskRepository.count(
                        TaskSpecification
                                .belongsToUser(currentUser)
                                .and(
                                        TaskSpecification.isOverdue()
                                )
                );

        LocalDateTime now = LocalDateTime.now();

        LocalDateTime startOfToday =
                now.toLocalDate().atStartOfDay();

        LocalDateTime endOfToday =
                startOfToday.plusDays(1);

        LocalDateTime startOfWeek =
                now.toLocalDate()
                        .minusDays(now.getDayOfWeek().getValue() - 1)
                        .atStartOfDay();

        LocalDateTime endOfWeek =
                startOfToday.plusDays(7);



        long tasksDueToday =
                taskRepository.count(
                        TaskSpecification
                                .belongsToUser(currentUser)
                                .and(
                                        TaskSpecification.deadlineBetween(
                                                startOfWeek,
                                                endOfWeek
                                        )
                                )
                );

        long tasksDueThisWeek =
                taskRepository.count(
                        TaskSpecification
                                .belongsToUser(currentUser)
                                .and(
                                        TaskSpecification.deadlineBetween(
                                                startOfWeek,
                                                endOfWeek
                                        )
                                )
                );

        long completedThisWeek =
                taskRepository.count(
                        TaskSpecification
                                .belongsToUser(currentUser)
                                .and(
                                        TaskSpecification.completedBetween(
                                                startOfWeek,
                                                endOfWeek
                                        )
                                )
                );

        long highPriorityTasks =
                taskRepository.count(
                        TaskSpecification
                                .belongsToUser(currentUser)
                                .and(TaskSpecification.priorityEquals(Priority.HIGH))
                                 .and(TaskSpecification.activeOnly())
                );

        long mediumPriorityTasks =
                taskRepository.count(
                        TaskSpecification
                                .belongsToUser(currentUser)
                                .and(TaskSpecification.priorityEquals(Priority.MEDIUM))
                                .and(TaskSpecification.activeOnly())
                );

        long lowPriorityTasks =
                taskRepository.count(
                        TaskSpecification
                                .belongsToUser(currentUser)
                                .and(TaskSpecification.priorityEquals(Priority.LOW))
                                .and(TaskSpecification.activeOnly())
                );

        long personalTasks =
                activityRepository.countByUserAndIsCollaborativeFalseAndStatusNot(
                        currentUser,
                        ActivityStatus.DELETED
                );

        long collaborativeTasks =
                activityRepository.countByUserAndIsCollaborativeTrueAndStatusNot(
                        currentUser,
                        ActivityStatus.DELETED
                );

        double completionRate = 0;

        if (activeTasks  > 0) {

            completionRate =
                    ((double) completedTasks / activeTasks ) * 100;

        }

        return TaskStatisticsResponse.builder()
                .activeTasks(activeTasks)
                .completedTasks(completedTasks)
                .pendingTasks(pendingTasks)
                .inProgressTasks(inProgressTasks)
                .overdueTasks(overdueTasks)

                .tasksDueToday(tasksDueToday)
                .tasksDueThisWeek(tasksDueThisWeek)
                .completedThisWeek(completedThisWeek)

                .highPriorityTasks(highPriorityTasks)
                .mediumPriorityTasks(mediumPriorityTasks)
                .lowPriorityTasks(lowPriorityTasks)

                .personalTasks(personalTasks)
                .collaborativeTasks(collaborativeTasks)

                .completionRate(completionRate)
                .build();
    }

}
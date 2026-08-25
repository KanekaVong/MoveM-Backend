package com.movem.backend.Service.Implement.TaskServices;

import com.movem.backend.Dto.request.TaskRequests.Create.CreateTaskRequest;
import com.movem.backend.Dto.request.TaskRequests.Search.TaskSearchCriteria;
import com.movem.backend.Dto.request.TaskRequests.Update.UpdateTaskRequest;
import com.movem.backend.Dto.response.TaskResponses.TaskResponse;
import com.movem.backend.Entity.Activity.Activity;
import com.movem.backend.Entity.Tasks.Task;
import com.movem.backend.Entity.Auth.User;
import com.movem.backend.Exception.ResourceNotFoundException;
import com.movem.backend.Mapper.TaskMapper.TaskMapper;
import com.movem.backend.Repository.SharedRepository.ActivityRepository;
import com.movem.backend.Service.Event.Factory.TaskEventFactory;
import com.movem.backend.Service.Event.FeatureEventTrackingService;
import com.movem.backend.Service.TaskServices.RecurringTaskService;import com.movem.backend.model.enums.Activity.ActivityStatus;
import com.movem.backend.model.enums.Activity.ActivityType;
import com.movem.backend.Repository.TaskRepositories.TaskRepository;
import com.movem.backend.Service.SharedServices.ActivityPermissionService;
import com.movem.backend.Service.SharedServices.ActivityService;
import com.movem.backend.Service.AuthServices.CurrentUserService;
import com.movem.backend.Service.TaskServices.TaskChecklistService;
import com.movem.backend.Service.TaskServices.TaskReminderService;
import com.movem.backend.Service.TaskServices.TaskService;
import com.movem.backend.model.enums.Priority;
import com.movem.backend.Service.TaskServices.Builder.TaskSearchBuilder;
import com.movem.backend.Specification.TaskSpecification;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;


@Service
@Transactional
@RequiredArgsConstructor
public class TaskServiceImpl implements TaskService {

    private final TaskRepository taskRepository;
    private final ActivityService activityService;
    private final CurrentUserService currentUserService;
    private final TaskChecklistService taskChecklistService;
    private final TaskReminderService taskReminderService;
    private final TaskMapper taskMapper;
    private final ActivityPermissionService activityPermissionService;
    private final TaskSearchBuilder taskSearchBuilder;
    private final RecurringTaskService recurringTaskService;
    private final ActivityRepository activityRepository;
    private final FeatureEventTrackingService featureEventTrackingService;
    private final TaskEventFactory taskEventFactory;

    @Override
    public TaskResponse createTask(CreateTaskRequest request) {


        User currentUser = currentUserService.getCurrentUser();

        Activity activity = activityService.createActivity(
                request,
                currentUser,
                ActivityType.TASK
        );

        Task task = new Task();

        task.setActivity(activity);
        task.setPriority(request.getPriority());
        task.setIsRecurring(request.getIsRecurring());
        task.setRecurringType(request.getRecurringType());

        task.setRecurringInterval(request.getRecurringInterval());

        task.setRecurringEndDate(request.getRecurringEndDate());

        Task savedTask = taskRepository.saveAndFlush(task);

        featureEventTrackingService.handle(
                taskEventFactory.taskCreated(activity, currentUser)
        );

        activityService.attachLabels(
                activity,
                request.getLabelIds()
        );

        taskChecklistService.createChecklistItems(
                savedTask,
                request.getChecklists()
        );

        taskReminderService.createReminders(
                savedTask,
                request.getReminders()
        );




        return taskMapper.toResponse(savedTask);
    }

    @Override
    @Transactional(readOnly = true)
    public TaskResponse getTask(String activityId) {

        User currentUser = currentUserService.getCurrentUser();

        Task task = taskRepository
                .findByActivityId(activityId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Task not found.")
                );

        activityPermissionService.validateCanEditActivity(
                task.getActivity(),
                currentUser
        );

        return taskMapper.toResponse(task);

    }

    @Override
    @Transactional(readOnly = true)
    public List<TaskResponse> getMyTasks() {

        User currentUser = currentUserService.getCurrentUser();

        return taskRepository.findAll(
                TaskSpecification
                        .belongsToUser(currentUser)
                        .and(
                                TaskSpecification.notDeleted()
                            )
                        )
                        .stream()
                        .map(taskMapper::toResponse)
                        .toList();
    }

    @Override
    @Transactional
    public TaskResponse updateTask(
            String activityId,
            UpdateTaskRequest request
    ) {

        User currentUser = currentUserService.getCurrentUser();

        Task task = taskRepository
                .findByActivityId(activityId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Task not found.")
                );

        Activity activity = task.getActivity();

        activityPermissionService.validateCanEditActivity(
                activity,
                currentUser
        );

        LocalDateTime oldDeadline = activity.getDeadline();
        Priority oldPriority = task.getPriority();
        Boolean oldRecurring = task.getIsRecurring();
        String oldTitle = activity.getActivityName();
        String oldDescription = activity.getDescription();

        activityService.updateActivity(activity, request);

        task.setPriority(request.getPriority());
        task.setIsRecurring(request.getIsRecurring());

        activity.getLabels().clear();

        activityService.attachLabels(
                activity,
                request.getLabelIds()
        );

        taskReminderService.syncDueDateReminders(task);

        Task updatedTask = taskRepository.save(task);

        if (activity.getStatus() == ActivityStatus.COMPLETE
                && Boolean.TRUE.equals(task.getIsRecurring())) {

            recurringTaskService.generateNextOccurrence(updatedTask);
        }

        boolean deadlineChanged =
                !Objects.equals(
                        oldDeadline,
                        activity.getDeadline()
                );

        boolean taskUpdated =
                !Objects.equals(
                        oldTitle,
                        activity.getActivityName()
                )
                        || !Objects.equals(
                        oldDescription,
                        activity.getDescription()
                )
                        || oldPriority != task.getPriority()
                        || !Objects.equals(
                        oldRecurring,
                        task.getIsRecurring()
                );

        if (!Objects.equals(
                oldRecurring,
                task.getIsRecurring()
        )) {

            featureEventTrackingService.handle(
                    taskEventFactory.taskUpdated(
                            activity,
                            currentUser,
                            "recurring",
                            "Updated recurring setting.",
                            String.valueOf(oldRecurring),
                            String.valueOf(
                                    task.getIsRecurring()
                            ),
                            false
                    )
            );
        }

        if (oldPriority != task.getPriority()) {

            featureEventTrackingService.handle(
                    taskEventFactory.taskUpdated(
                            activity,
                            currentUser,
                            "priority",
                            "Updated task priority.",
                            oldPriority.name(),
                            task.getPriority().name(),
                            false
                    )
            );
        }

        if (!Objects.equals(
                oldDescription,
                activity.getDescription()
        )) {

            featureEventTrackingService.handle(
                    taskEventFactory.taskUpdated(
                            activity,
                            currentUser,
                            "description",
                            "Updated task description.",
                            oldDescription,
                            activity.getDescription(),
                            false
                    )
            );
        }

        if (!Objects.equals(
                oldTitle,
                activity.getActivityName()
        )) {

            featureEventTrackingService.handle(
                    taskEventFactory.taskUpdated(
                            activity,
                            currentUser,
                            "title",
                            "Updated task title.",
                            oldTitle,
                            activity.getActivityName(),
                            false
                    )
            );
        }

        if (taskUpdated) {

            featureEventTrackingService.handle(
                    taskEventFactory.taskUpdated(
                            activity,
                            currentUser,
                            null,
                            "Updated task.",
                            null,
                            null,
                            true
                    )
            );
        }

        if (deadlineChanged) {

            featureEventTrackingService.handle(
                    taskEventFactory.deadlineChanged(
                            activity,
                            currentUser,
                            oldDeadline,
                            activity.getDeadline()
                    )
            );
        }

        return taskMapper.toResponse(updatedTask);
    }

    @Override
    @Transactional
    public void deleteTask(String activityId) {

        User currentUser = currentUserService.getCurrentUser();

        Task task = taskRepository
                .findByActivityId(activityId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Task not found."
                        )
                );

        Activity activity = task.getActivity();

        activityPermissionService.validateActivityOwner(
                activity,
                currentUser
        );


        activity.setStatus(ActivityStatus.DELETED);
        activity.setDeletedAt(LocalDateTime.now());
        activity.setUpdatedAt(LocalDateTime.now());

        featureEventTrackingService.handle(
                taskEventFactory.taskDeleted(activity, currentUser)
        );

        activity.setUpdatedAt(LocalDateTime.now());
    }

    @Override
    @Transactional
    public TaskResponse restoreTask(String activityId) {

        User currentUser = currentUserService.getCurrentUser();

        Task task = taskRepository
                .findByActivityId(activityId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Task not found.")
                );

        Activity activity = task.getActivity();

        activityPermissionService.validateActivityOwner(
                activity,
                currentUser
        );

        if (activity.getStatus() != ActivityStatus.DELETED) {
            throw new IllegalStateException(
                    "Task is not deleted."
            );
        }

        activity.setStatus(ActivityStatus.PENDING);
        activity.setDeletedAt(null);
        activity.setUpdatedAt(LocalDateTime.now());

        featureEventTrackingService.handle(
                taskEventFactory.taskRestored(activity, currentUser)
        );

        activity.setUpdatedAt(LocalDateTime.now());

        return taskMapper.toResponse(task);
    }

    @Override
    @Transactional(readOnly = true)
    public List<TaskResponse> searchTasks(
            String search,
            ActivityStatus status,
            Priority priority,
            Integer labelId,
            String sortBy,
            String direction,
            Boolean overdue,
            Integer upcomingDays,
            Boolean active
    ) {

        User currentUser = currentUserService.getCurrentUser();

        TaskSearchCriteria criteria =
                TaskSearchCriteria.builder()
                        .search(search)
                        .status(status)
                        .priority(priority)
                        .labelId(labelId)
                        .sortBy(sortBy)
                        .direction(direction)
                        .overdue(overdue)
                        .upcomingDays(upcomingDays)
                        .active(active)
                        .build();

        Specification<Task> specification =
                taskSearchBuilder.buildSpecification(
                        currentUser,
                        criteria
                );

        Sort sort =
                taskSearchBuilder.buildSort(criteria);

        return taskRepository
                .findAll(specification, sort)
                .stream()
                .map(taskMapper::toResponse)
                .toList();
    }

    @Override
    @Transactional
    public TaskResponse completeTask(String activityId) {

        User currentUser = currentUserService.getCurrentUser();

        Task task = taskRepository
                .findByActivityId(activityId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Task not found."
                        )
                );

        Activity activity = task.getActivity();

        activityPermissionService.validateCanEditActivity(
                activity,
                currentUser
        );


        if (activity.getStatus() == ActivityStatus.COMPLETE) {
            throw new IllegalStateException(
                    "Task is already completed."
            );
        }

        activity.setStatus(ActivityStatus.COMPLETE);

        featureEventTrackingService.handle(
                taskEventFactory.taskCompleted(activity, currentUser)
        );
        activity.setUpdatedAt(LocalDateTime.now());

        activityRepository.save(activity);

        if (Boolean.TRUE.equals(task.getIsRecurring())) {

            recurringTaskService.generateNextOccurrence(task);

        }

        return taskMapper.toResponse(task);
    }
}
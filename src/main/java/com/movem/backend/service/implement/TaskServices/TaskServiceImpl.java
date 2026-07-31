package com.movem.backend.service.implement.TaskServices;

import com.movem.backend.dto.request.TaskRequests.Create.CreateTaskRequest;
import com.movem.backend.dto.request.TaskRequests.Search.TaskSearchCriteria;
import com.movem.backend.dto.request.TaskRequests.Update.UpdateTaskRequest;
import com.movem.backend.dto.response.TaskResponses.TaskResponse;
import com.movem.backend.entity.Activity.Activity;
import com.movem.backend.entity.Group.ActivityGroup;
import com.movem.backend.entity.Tasks.Task;
import com.movem.backend.entity.User;
import com.movem.backend.exception.ResourceNotFoundException;
import com.movem.backend.exception.UnauthorizedActionException;
import com.movem.backend.mapper.TaskMapper.TaskMapper;
import com.movem.backend.model.enums.Activity.ActivityFeedEvent;
import com.movem.backend.model.enums.Activity.ActivityStatus;
import com.movem.backend.model.enums.Activity.ActivityType;
import com.movem.backend.model.enums.Audit.AuditCategory;
import com.movem.backend.model.enums.Audit.AuditSeverity;
import com.movem.backend.repository.GroupRepository.GroupRepository;
import com.movem.backend.repository.TaskRepositories.TaskRepository;
import com.movem.backend.service.SharedServices.ActivityFeedService;
import com.movem.backend.service.SharedServices.ActivityPermissionService;
import com.movem.backend.service.SharedServices.ActivityService;
import com.movem.backend.service.AuthServices.CurrentUserService;
import com.movem.backend.service.SharedServices.AuditLogService;
import com.movem.backend.service.TaskServices.TaskChecklistService;
import com.movem.backend.service.TaskServices.TaskReminderService;
import com.movem.backend.service.TaskServices.TaskService;
import com.movem.backend.model.enums.Priority;
import com.movem.backend.service.builder.TaskSearchBuilder;
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
    private final ActivityFeedService activityFeedService;
    private final AuditLogService auditLogService;

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

        Task savedTask = taskRepository.save(task);

        activityFeedService.createFeed(
                activity,
                currentUser,
                ActivityFeedEvent.TASK_CREATED,
                "created the task.",
                null
        );
        auditLogService.createLog(
                activity,
                currentUser,
                ActivityFeedEvent.TASK_CREATED,
                AuditCategory.TASK,
                AuditSeverity.INFO,
                null,
                "Created task.",
                null,
                null
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

        return taskRepository
                .findAllByActivityUserAndActivityStatusNot(
                        currentUser,
                        ActivityStatus.DELETED
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
                        new ResourceNotFoundException(
                                "Task not found."
                        )
                );

        Activity activity = task.getActivity();

        // Allow activity owner or group member
        activityPermissionService.validateCanEditActivity(
                activity,
                currentUser
        );

        // Save old values for feed events
        LocalDateTime oldDeadline = activity.getDeadline();
        Priority oldPriority = task.getPriority();
        Boolean oldRecurring = task.getIsRecurring();
        String oldTitle = activity.getActivityName();
        String oldDescription = activity.getDescription();

        // Update activity
        activityService.updateActivity(activity, request);

        // Update task
        task.setPriority(request.getPriority());
        task.setIsRecurring(request.getIsRecurring());

        // Labels
        activity.getLabels().clear();
        activityService.attachLabels(
                activity,
                request.getLabelIds()
        );

        // Reminders
        taskReminderService.syncDueDateReminders(task);

        Task updatedTask = taskRepository.save(task);

        /*
         * Feed Events
         */

        boolean deadlineChanged =
                !Objects.equals(
                        oldDeadline,
                        activity.getDeadline()
                );

        boolean taskUpdated =
                !Objects.equals(oldTitle, activity.getActivityName())
                        || !Objects.equals(oldDescription, activity.getDescription())
                        || oldPriority != task.getPriority()
                        || !Objects.equals(oldRecurring, task.getIsRecurring());

        if (!Objects.equals(oldRecurring, task.getIsRecurring())) {

            auditLogService.createLog(
                    activity,
                    currentUser,
                    ActivityFeedEvent.TASK_UPDATED,
                    AuditCategory.TASK,
                    AuditSeverity.INFO,
                    "recurring",
                    "Updated recurring setting.",
                    String.valueOf(oldRecurring),
                    String.valueOf(task.getIsRecurring())
            );

        }

        if (oldPriority != task.getPriority()) {

            auditLogService.createLog(
                    activity,
                    currentUser,
                    ActivityFeedEvent.TASK_UPDATED,
                    AuditCategory.TASK,
                    AuditSeverity.INFO,
                    "priority",
                    "Updated task priority.",
                    oldPriority.name(),
                    task.getPriority().name()
            );

        }

        if (!Objects.equals(oldDescription, activity.getDescription())) {

            auditLogService.createLog(
                    activity,
                    currentUser,
                    ActivityFeedEvent.TASK_UPDATED,
                    AuditCategory.TASK,
                    AuditSeverity.INFO,
                    "description",
                    "Updated task description.",
                    oldDescription,
                    activity.getDescription()
            );

        }

        if (!Objects.equals(oldTitle, activity.getActivityName())) {

            auditLogService.createLog(
                    activity,
                    currentUser,
                    ActivityFeedEvent.TASK_UPDATED,
                    AuditCategory.TASK,
                    AuditSeverity.INFO,
                    "title",
                    "Updated task title.",
                    oldTitle,
                    activity.getActivityName()
            );

        }

        if (taskUpdated) {

            activityFeedService.createFeed(
                    activity,
                    currentUser,
                    ActivityFeedEvent.TASK_UPDATED,
                    "updated the task.",
                    null
            );

        }

        if (deadlineChanged) {

            activityFeedService.createFeed(
                    activity,
                    currentUser,
                    ActivityFeedEvent.DEADLINE_CHANGED,
                    "changed the deadline.",
                    null
            );

            auditLogService.createLog(
                    activity,
                    currentUser,
                    ActivityFeedEvent.DEADLINE_CHANGED,
                    AuditCategory.TASK,
                    AuditSeverity.WARNING,
                    "deadline",
                    "Changed deadline.",
                    oldDeadline == null ? null : oldDeadline.toString(),
                    activity.getDeadline() == null ? null : activity.getDeadline().toString()
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

        auditLogService.createLog(
                activity,
                currentUser,
                ActivityFeedEvent.TASK_DELETED,
                AuditCategory.TASK,
                AuditSeverity.CRITICAL,
                "status",
                "Deleted task.",
                ActivityStatus.PENDING.name(),
                ActivityStatus.DELETED.name()
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

        auditLogService.createLog(
                activity,
                currentUser,
                ActivityFeedEvent.TASK_RESTORED,
                AuditCategory.TASK,
                AuditSeverity.WARNING,
                "status",
                "Restored task.",
                ActivityStatus.DELETED.name(),
                ActivityStatus.PENDING.name()
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

        // Allow activity owner or group member
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

        activityFeedService.createFeed(
                activity,
                currentUser,
                ActivityFeedEvent.TASK_COMPLETED,
                "completed the task.",
                null
        );

        auditLogService.createLog(
                activity,
                currentUser,
                ActivityFeedEvent.TASK_COMPLETED,
                AuditCategory.TASK,
                AuditSeverity.INFO,
                "status",
                "Completed task.",
                ActivityStatus.PENDING.name(),
                ActivityStatus.COMPLETE.name()
        );

        activity.setUpdatedAt(LocalDateTime.now());

        return taskMapper.toResponse(task);
    }

}
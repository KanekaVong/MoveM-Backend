package com.movem.backend.Service.Implement.TaskServices;

import com.movem.backend.Entity.Activity.Activity;
import com.movem.backend.Entity.Tasks.Task;
import com.movem.backend.Entity.Tasks.TaskChecklist;
import com.movem.backend.Entity.Tasks.TaskLabel;
import com.movem.backend.Entity.Tasks.TaskReminder;
import com.movem.backend.Repository.SharedRepository.ActivityRepository;
import com.movem.backend.Repository.TaskRepositories.TaskChecklistRepository;
import com.movem.backend.Repository.TaskRepositories.TaskLabelRepository;
import com.movem.backend.Repository.TaskRepositories.TaskReminderRepository;
import com.movem.backend.Repository.TaskRepositories.TaskRepository;
import com.movem.backend.Service.SharedServices.ActivityFeedService;
import com.movem.backend.Service.SharedServices.AuditLogService;
import com.movem.backend.Service.TaskServices.RecurringTaskService;
import com.movem.backend.Util.ActivityIdGenerator;
import com.movem.backend.model.enums.Activity.ActivityFeedEvent;
import com.movem.backend.model.enums.Activity.ActivityStatus;
import com.movem.backend.model.enums.Audit.AuditCategory;
import com.movem.backend.model.enums.Audit.AuditSeverity;
import com.movem.backend.model.enums.RecurringType;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import static com.movem.backend.model.enums.ReminderType.*;

@Service
@RequiredArgsConstructor
@Transactional
public class RecurringTaskServiceImpl implements RecurringTaskService {

    private final ActivityRepository activityRepository;

    private final TaskRepository taskRepository;

    private final TaskLabelRepository taskLabelRepository;

    private final TaskChecklistRepository checklistRepository;

    private final TaskReminderRepository reminderRepository;

    private final ActivityFeedService activityFeedService;

    private final ActivityIdGenerator activityIdGenerator;

    private final AuditLogService auditLogService;

    @Override
    public void generateNextOccurrence(
            Task completedTask
    ) {

        System.out.println("===== generateNextOccurrence CALLED =====");

        System.out.println("Recurring = " + completedTask.getIsRecurring());

        if (!Boolean.TRUE.equals(completedTask.getIsRecurring())) {
            System.out.println("EXIT 1 -> Not recurring");
            return;
        }

        System.out.println("Recurring Type = " + completedTask.getRecurringType());

        if (completedTask.getRecurringType() == null) {
            System.out.println("EXIT 2 -> Recurring type is null");
            return;
        }

        Activity oldActivity = completedTask.getActivity();

        System.out.println("Start = " + oldActivity.getStartActivity());
        System.out.println("Deadline = " + oldActivity.getDeadline());

        if (oldActivity.getStartActivity() == null
                || oldActivity.getDeadline() == null) {
            System.out.println("EXIT 3 -> Start or deadline is null");
            return;
        }

        LocalDateTime newStart =
                calculateNextDate(
                        oldActivity.getStartActivity(),
                        completedTask.getRecurringType(),
                        completedTask.getRecurringInterval()
                );

        LocalDateTime newDeadline =
                calculateNextDate(
                        oldActivity.getDeadline(),
                        completedTask.getRecurringType(),
                        completedTask.getRecurringInterval()
                );

        System.out.println("New Start = " + newStart);
        System.out.println("New Deadline = " + newDeadline);

        if (completedTask.getRecurringEndDate() != null
                && newStart.toLocalDate()
                .isAfter(completedTask.getRecurringEndDate())) {

            System.out.println("EXIT 4 -> Recurring end date reached");
            return;

        }

        System.out.println(">>> Cloning Activity");

        Activity newActivity =
                cloneActivity(
                        oldActivity,
                        newStart,
                        newDeadline
                );

        System.out.println("New Activity ID = " + newActivity.getId());

        System.out.println(">>> Cloning Task");

        Task newTask =
                cloneTask(
                        completedTask,
                        newActivity
                );

        System.out.println("New Task Activity ID = " + newTask.getActivity().getId());

        System.out.println(">>> Cloning Labels");

        cloneLabels(
                oldActivity,
                newActivity
        );

        System.out.println(">>> Cloning Checklist");

        cloneChecklist(
                completedTask,
                newTask
        );

        System.out.println(">>> Cloning Reminders");

        cloneReminders(
                completedTask,
                newTask
        );

        System.out.println(">>> Creating Feed");

        activityFeedService.createFeed(
                newActivity,
                newActivity.getUser(),
                ActivityFeedEvent.TASK_RECURRED,
                "generated the next recurring task.",
                null
        );

        System.out.println(">>> Creating Audit");

        auditLogService.createLog(
                newActivity,
                newActivity.getUser(),
                ActivityFeedEvent.TASK_RECURRED,
                AuditCategory.TASK,
                AuditSeverity.INFO,
                null,
                "Generated next recurring task.",
                null,
                null
        );

        System.out.println("===== RECURRING TASK CREATED SUCCESSFULLY =====");
    }

    private LocalDateTime calculateNextDate(
            LocalDateTime current,
            RecurringType recurringType,
            Integer interval
    ) {

        int step = interval == null || interval <= 0
                ? 1
                : interval;

        return switch (recurringType) {

            case DAILY -> current.plusDays(step);

            case WEEKLY -> current.plusWeeks(step);

            case MONTHLY -> current.plusMonths(step);

            case YEARLY -> current.plusYears(step);
        };

    }

    private Activity cloneActivity(
            Activity oldActivity,
            LocalDateTime newStart,
            LocalDateTime newDeadline
    ) {

        Activity newActivity = new Activity();

        newActivity.setId(activityIdGenerator.generate());

        newActivity.setActivityName(oldActivity.getActivityName());

        newActivity.setActivityType(oldActivity.getActivityType());

        newActivity.setUser(oldActivity.getUser());

        newActivity.setStatus(ActivityStatus.PENDING);

        newActivity.setStartActivity(newStart);

        newActivity.setDeadline(newDeadline);

        newActivity.setDescription(oldActivity.getDescription());

        newActivity.setLocationName(oldActivity.getLocationName());

        newActivity.setLocationAddress(oldActivity.getLocationAddress());

        newActivity.setLat(oldActivity.getLat());

        newActivity.setLng(oldActivity.getLng());

        newActivity.setGooglePlaceId(oldActivity.getGooglePlaceId());

        newActivity.setCoordinates(oldActivity.getCoordinates());

        newActivity.setParentActivity(null);

        newActivity.setCreatedAt(LocalDateTime.now());

        newActivity.setUpdatedAt(LocalDateTime.now());

        newActivity.setDeletedAt(null);

        newActivity.setIsCollaborative(oldActivity.getIsCollaborative());

        return activityRepository.save(newActivity);

    }

    private Task cloneTask(
            Task oldTask,
            Activity newActivity
    ) {

        Task newTask = new Task();

        newTask.setActivity(newActivity);

        newTask.setPriority(oldTask.getPriority());

        newTask.setIsRecurring(oldTask.getIsRecurring());

        newTask.setRecurringType(oldTask.getRecurringType());

        newTask.setRecurringInterval(oldTask.getRecurringInterval());

        newTask.setRecurringEndDate(oldTask.getRecurringEndDate());

        return taskRepository.save(newTask);

    }

    private void cloneLabels(
            Activity oldActivity,
            Activity newActivity
    ) {
        newActivity.setLabels(
                new HashSet<>(oldActivity.getLabels())
        );

        activityRepository.save(newActivity);
    }

    private void cloneChecklist(
            Task oldTask,
            Task newTask
    ) {
        List<TaskChecklist> oldItems =
                checklistRepository.findByTask(oldTask);

        List<TaskChecklist> newItems = new ArrayList<>();

        for (TaskChecklist oldItem : oldItems) {

            TaskChecklist newItem = new TaskChecklist();

            newItem.setTask(newTask);

            newItem.setItemName(oldItem.getItemName());

            newItem.setIsCompleted(false);

            newItem.setCreatedAt(LocalDateTime.now());

            newItems.add(newItem);
        }

        checklistRepository.saveAll(newItems);

    }

    private void cloneReminders(
            Task oldTask,
            Task newTask
    ) {

        List<TaskReminder> oldReminders =
                reminderRepository.findByTask(oldTask);

        List<TaskReminder> newReminders =
                new ArrayList<>();

        for (TaskReminder oldReminder : oldReminders) {

            TaskReminder newReminder = new TaskReminder();

            newReminder.setTask(newTask);

            newReminder.setType(oldReminder.getType());

            newReminder.setIsSent(false);

            newReminder.setCreatedAt(LocalDateTime.now());

            switch (oldReminder.getType()) {

                case DUE_DATE ->

                        newReminder.setRemindAt(
                                newTask.getActivity().getDeadline()
                        );

                case START_DATE ->

                        newReminder.setRemindAt(
                                newTask.getActivity().getStartActivity()
                        );

                case CUSTOM -> {

                    Duration difference =
                            Duration.between(
                                    oldReminder.getRemindAt(),
                                    oldTask.getActivity().getDeadline()
                            );

                    newReminder.setRemindAt(
                            newTask.getActivity()
                                    .getDeadline()
                                    .minus(difference)
                    );

                }

            }

            newReminders.add(newReminder);

        }

        reminderRepository.saveAll(newReminders);

    }
}

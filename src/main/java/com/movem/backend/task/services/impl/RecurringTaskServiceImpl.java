package com.movem.backend.task.services.impl;

import com.movem.backend.shared.activity.entities.Activity;
import com.movem.backend.task.entities.Task;
import com.movem.backend.shared.checklist.entities.Checklist;
import com.movem.backend.shared.reminder.entities.Reminder;
import com.movem.backend.shared.activity.repositories.ActivityRepository;
import com.movem.backend.shared.checklist.repository.ChecklistRepository;
import com.movem.backend.shared.reminder.repository.ReminderRepository;
import com.movem.backend.task.repositories.TaskRepository;
import com.movem.backend.commons.Event.Factory.TaskEventFactory;
import com.movem.backend.shared.historyandlogs.featureevents.services.FeatureEventTrackingService;
import com.movem.backend.task.services.RecurringTaskService;
import com.movem.backend.commons.Util.ActivityIdGenerator;
import com.movem.backend.commons.enums.shared.ActivityStatus;
import com.movem.backend.commons.enums.Task.RecurringType;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class RecurringTaskServiceImpl implements RecurringTaskService {

    private final ActivityRepository activityRepository;
    private final TaskRepository taskRepository;
    private final ChecklistRepository checklistRepository;
    private final ReminderRepository reminderRepository;
    private final ActivityIdGenerator activityIdGenerator;
    private final FeatureEventTrackingService featureEventTrackingService;
    private final TaskEventFactory taskEventFactory;

    @Override
    public void generateNextOccurrence(
            Task completedTask
    ) {

        if (!Boolean.TRUE.equals(completedTask.getIsRecurring())) {
            System.out.println("EXIT 1 -> Not recurring");
            return;
        }


        if (completedTask.getRecurringType() == null) {
            System.out.println("EXIT 2 -> Recurring type is null");
            return;
        }

        Activity oldActivity = completedTask.getActivity();

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

        if (completedTask.getRecurringEndDate() != null
                && newStart.toLocalDate()
                .isAfter(completedTask.getRecurringEndDate())) {

            System.out.println("EXIT 4 -> Recurring end date reached");
            return;

        }


        Activity newActivity =
                cloneActivity(
                        oldActivity,
                        newStart,
                        newDeadline
                );

        Task newTask =
                cloneTask(
                        completedTask,
                        newActivity
                );

        cloneLabels(
                oldActivity,
                newActivity
        );

        cloneChecklist(
                completedTask,
                newTask
        );


        cloneReminders(
                completedTask,
                newTask
        );


        featureEventTrackingService.handle(
                taskEventFactory.taskRecurred(
                        newActivity,
                        newActivity.getUser()
                )
        );

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
        List<Checklist> oldItems =
                checklistRepository.findByTask(oldTask);

        List<Checklist> newItems = new ArrayList<>();

        for (Checklist oldItem : oldItems) {

            Checklist newItem = new Checklist();

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

        List<Reminder> oldReminders =
                reminderRepository.findByTask(oldTask);

        List<Reminder> newReminders =
                new ArrayList<>();

        for (Reminder oldReminder : oldReminders) {

            Reminder newReminder = new Reminder();

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

package com.movem.backend.shared.reminder.services.impl;

import com.movem.backend.shared.reminder.dtos.requests.CreateReminderRequest;
import com.movem.backend.shared.reminder.dtos.requests.UpdateReminderRequest;
import com.movem.backend.shared.reminder.dtos.responses.UpcomingReminderResponse;
import com.movem.backend.shared.reminder.dtos.responses.ReminderResponse;
import com.movem.backend.task.entities.Task;
import com.movem.backend.shared.reminder.entities.Reminder;
import com.movem.backend.authentication.entities.User;
import com.movem.backend.trip.entities.Trip;
import com.movem.backend.commons.Exception.ResourceNotFoundException;
import com.movem.backend.shared.reminder.mapper.ReminderMapper;
import com.movem.backend.shared.notification.services.NotificationService;
import com.movem.backend.commons.enums.shared.ActivityType;
import com.movem.backend.commons.enums.shared.ReminderType;
import com.movem.backend.shared.reminder.repository.ReminderRepository;
import com.movem.backend.task.repositories.TaskRepository;
import com.movem.backend.authentication.services.CurrentUserService;
import com.movem.backend.shared.reminder.services.ReminderService;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.movem.backend.commons.enums.Notification.NotificationType.TASK_REMINDER;
import static com.movem.backend.commons.enums.Notification.NotificationType.TRIP_REMINDER;
import static com.movem.backend.commons.enums.Notification.ReferenceType.TASK;
import static com.movem.backend.commons.enums.Notification.ReferenceType.TRIP;

@Service
@RequiredArgsConstructor
public class ReminderServiceImpl implements ReminderService {

    private final TaskRepository taskRepository;
    private final ReminderMapper reminderMapper;
    private final NotificationService notificationService;
    private final ReminderRepository reminderRepository;
    private final CurrentUserService currentUserService;

    @Override
    public void createReminders(Task task, List<CreateReminderRequest> reminders) {

        LocalDateTime now = LocalDateTime.now();
        LocalDateTime startDate = task.getActivity().getStartActivity();
        LocalDateTime deadline = task.getActivity().getDeadline();

        if (startDate != null && !startDate.isBefore(now)) {
            Reminder startReminder = new Reminder();

            startReminder.setTask(task);
            startReminder.setType(ReminderType.START_DATE);
            startReminder.setRemindAt(startDate);
            startReminder.setIsSent(false);
            startReminder.setCreatedAt(now);

            reminderRepository.save(startReminder);
        }

        if (deadline != null) {
            LocalDateTime oneWeekBefore = deadline.minusDays(7);

            if (!oneWeekBefore.isBefore(now)) {
                Reminder oneWeekReminder = new Reminder();

                oneWeekReminder.setTask(task);
                oneWeekReminder.setType(ReminderType.ONE_WEEK_BEFORE);
                oneWeekReminder.setRemindAt(oneWeekBefore);
                oneWeekReminder.setIsSent(false);
                oneWeekReminder.setCreatedAt(now);

                reminderRepository.save(oneWeekReminder);
            }

            if (!deadline.isBefore(now)) {
                Reminder dueReminder = new Reminder();

                dueReminder.setTask(task);
                dueReminder.setType(ReminderType.DUE_DATE);
                dueReminder.setRemindAt(deadline);
                dueReminder.setIsSent(false);
                dueReminder.setCreatedAt(now);

                reminderRepository.save(dueReminder);
            }
        }

        if (reminders != null && !reminders.isEmpty()) {

            List<Reminder> customReminders = new ArrayList<>();

            for (CreateReminderRequest request : reminders) {
                if (request.getType() != ReminderType.CUSTOM) {
                    throw new IllegalArgumentException(
                            "Only CUSTOM reminders can be manually created for tasks."
                    );
                }

                if (request.getRemindAt() == null) {
                    throw new IllegalArgumentException(
                            "Custom reminder time is required."
                    );
                }

                Reminder customReminder = new Reminder();

                customReminder.setTask(task);
                customReminder.setType(ReminderType.CUSTOM);
                customReminder.setRemindAt(request.getRemindAt());
                customReminder.setIsSent(false);
                customReminder.setCreatedAt(now);

                customReminders.add(customReminder);
            }

            reminderRepository.saveAll(customReminders);
        }
    }

    @Override
    @Transactional(readOnly = true)
    public List<UpcomingReminderResponse> getUpcomingReminders() {
        User currentUser = currentUserService.getCurrentUser();

        LocalDateTime now = LocalDate.now().atStartOfDay();
        LocalDateTime nextWeek = now.plusDays(7).withHour(23).withMinute(59).withSecond(59).withNano(999999999);

        List<Reminder> upcoming = reminderRepository.findByTaskActivityUserAndRemindAtAfterAndRemindAtBeforeOrderByRemindAtAsc(
                                currentUser, now, nextWeek);
        return upcoming.stream().map(reminder -> UpcomingReminderResponse.builder()
                                .activityId(reminder.getTask().getActivity().getId())
                                .activityName(reminder.getTask().getActivity().getActivityName()).activityType(ActivityType.TASK)
                                .remindAt(reminder.getRemindAt())
                                .reminderType(reminder.getType())
                                .build()).toList();
    }

    @Override
    public List<ReminderResponse> getTaskReminders(
            String activityId) {

        Task task = taskRepository.findByActivityId(activityId).orElseThrow(() -> new ResourceNotFoundException("Task not found."));

        return reminderRepository
                .findByTaskOrderByRemindAtAsc(task)
                .stream()
                .map(reminderMapper::toResponse)
                .toList();
    }

    @Override
    public void addReminder(String activityId, CreateReminderRequest request) {

        Task task = taskRepository.findByActivityId(activityId).orElseThrow(() -> new ResourceNotFoundException("Task not found."));

        if (request.getType() != ReminderType.CUSTOM) {
            throw new IllegalArgumentException("Only CUSTOM reminders can be manually created.");
        }

        if (request.getRemindAt() == null) {
            throw new IllegalArgumentException("Custom reminder time is required.");
        }

        Reminder reminder = new Reminder();

        reminder.setTask(task);
        reminder.setType(ReminderType.CUSTOM);
        reminder.setRemindAt(request.getRemindAt());
        reminder.setCreatedAt(LocalDateTime.now());
        reminder.setIsSent(false);

        reminderRepository.save(reminder);
    }

    @Override
    public void updateReminder(Integer reminderId, UpdateReminderRequest request) {

        Reminder reminder = reminderRepository.findById(reminderId).orElseThrow(() -> new ResourceNotFoundException("Reminder not found."));


        if (reminder.getType() != ReminderType.CUSTOM) {
            throw new IllegalArgumentException("Only CUSTOM reminders can be manually updated.");
        }

        reminder.setType(ReminderType.CUSTOM);
        reminder.setRemindAt(request.getRemindAt());
        reminder.setIsSent(false);

        reminderRepository.save(reminder);
    }

    @Override
    public void deleteReminder(Integer reminderId) {

        Reminder reminder = reminderRepository.findById(reminderId)
                        .orElseThrow(() -> new ResourceNotFoundException("Reminder not found."));

        reminderRepository.delete(reminder);
    }

    @Override
    public void syncTaskReminders(Task task) {

        LocalDateTime now = LocalDateTime.now();
        LocalDateTime startDate = task.getActivity().getStartActivity();
        LocalDateTime deadline = task.getActivity().getDeadline();

        List<Reminder> reminders = reminderRepository.findByTask(task);

        Optional<Reminder> startReminder = reminders.stream().filter(r -> r.getType() == ReminderType.START_DATE).findFirst();

        if (startDate != null && !startDate.isBefore(now)) {

            Reminder reminder = startReminder.orElseGet(() -> {

                Reminder newReminder = new Reminder();

                newReminder.setTask(task);
                newReminder.setType(ReminderType.START_DATE);
                newReminder.setCreatedAt(now);

                return newReminder;
            });

            reminder.setRemindAt(startDate);
            reminder.setIsSent(false);

            reminderRepository.save(reminder);

        } else {
            startReminder.ifPresent(reminderRepository::delete);
        }

        Optional<Reminder> oneWeekReminder = reminders.stream().filter(r -> r.getType() == ReminderType.ONE_WEEK_BEFORE).findFirst();

        if (deadline != null) {
            LocalDateTime oneWeekBefore = deadline.minusDays(7);

            if (!oneWeekBefore.isBefore(now)) {
                Reminder reminder = oneWeekReminder.orElseGet(() -> {
                            Reminder newReminder = new Reminder();

                            newReminder.setTask(task);
                            newReminder.setType(ReminderType.ONE_WEEK_BEFORE);
                            newReminder.setCreatedAt(now);

                            return newReminder;
                        });

                reminder.setRemindAt(oneWeekBefore);
                reminder.setIsSent(false);

                reminderRepository.save(reminder);

            } else {
                oneWeekReminder.ifPresent(reminderRepository::delete);
            }

        } else {
            oneWeekReminder.ifPresent(reminderRepository::delete);
        }

        Optional<Reminder> dueReminder = reminders.stream()
                        .filter(r -> r.getType() == ReminderType.DUE_DATE)
                        .findFirst();

        if (deadline != null && !deadline.isBefore(now)) {
            Reminder reminder = dueReminder.orElseGet(() -> {

                        Reminder newReminder = new Reminder();

                        newReminder.setTask(task);
                        newReminder.setType(ReminderType.DUE_DATE);
                        newReminder.setCreatedAt(now);
                        return newReminder;
                    });

            reminder.setRemindAt(deadline);
            reminder.setIsSent(false);

            reminderRepository.save(reminder);

        } else {

            dueReminder.ifPresent(reminderRepository::delete);
        }
    }

    @Override
    @Transactional
    public void processDueReminders() {
        LocalDateTime now = LocalDateTime.now();
        System.out.println("PROCESSING REMINDERS: " + now);
        List<Reminder> dueReminders = reminderRepository.findByRemindAtLessThanEqualAndIsSentFalse(now);
        System.out.println("DUE REMINDERS: " + dueReminders.size());


        for (Reminder reminder : dueReminders) {

            if (reminder.getTask() != null) {

                Task task = reminder.getTask();
                User user = task.getActivity().getUser();

                String title;
                String message;


                if (reminder.getType() == ReminderType.START_DATE) {

                    title = "Task Starting";
                    message = "Your task \"" + task.getActivity().getActivityName() + "\" is starting now.";


                } else if (reminder.getType() == ReminderType.ONE_WEEK_BEFORE) {

                    title = "Upcoming Task";
                    message = "Your task \"" + task.getActivity().getActivityName() + "\" is due in one week.";


                } else if (reminder.getType() == ReminderType.DUE_DATE) {

                    title = "Task Due";
                    message = "Your task \"" + task.getActivity().getActivityName() + "\" is due.";


                } else {
                    title = "Task Reminder";
                    message = "Reminder for your task \"" + task.getActivity().getActivityName() + "\".";
                }

                notificationService.createNotification(user, user, TASK_REMINDER, TASK, task.getActivity().getId(), title, message);
            }
            else if (reminder.getTrip() != null) {
                Trip trip = reminder.getTrip();
                User user = trip.getActivity().getUser();

                String title;
                String message;

                if (reminder.getType() == ReminderType.ONE_WEEK_BEFORE) {
                    title = "Upcoming Trip";
                    message = "Your trip \"" + trip.getActivity().getActivityName() + "\" is one week away.";
                } else if (reminder.getType() == ReminderType.START_DATE) {
                    title = "Your Trip Starts Today!";
                    message = "Are you excited for your trip today? " + "Start your trip with us now.";
                } else {
                    continue;
                }

                notificationService.createNotification(user, user, TRIP_REMINDER, TRIP, trip.getActivity().getId(), title, message);
            }
            reminder.setIsSent(true);
        }
        reminderRepository.saveAll(dueReminders);
    }

    @Scheduled(fixedRate = 60000)
    @Transactional
    public void processDueReminderJob() {
        processDueReminders();
    }

    @Override
    public void addCustomReminders(Task task, List<CreateReminderRequest> reminders) {
        if (reminders == null || reminders.isEmpty()) {
            return;
        }
        List<Reminder> reminderList = new ArrayList<>();

        for (CreateReminderRequest request : reminders) {
            if (request.getType() != ReminderType.CUSTOM) {
                throw new IllegalArgumentException("Only CUSTOM reminders can be added manually.");
            }
            if (request.getRemindAt() == null) {
                throw new IllegalArgumentException("Custom reminder time is required.");
            }
            Reminder reminder = new Reminder();

            reminder.setTask(task);
            reminder.setType(ReminderType.CUSTOM);
            reminder.setRemindAt(request.getRemindAt());
            reminder.setIsSent(false);
            reminder.setCreatedAt(LocalDateTime.now());

            reminderList.add(reminder);
        }

        reminderRepository.saveAll(reminderList);
    }
}
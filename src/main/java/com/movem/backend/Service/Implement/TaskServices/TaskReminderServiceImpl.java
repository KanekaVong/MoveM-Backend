package com.movem.backend.Service.Implement.TaskServices;

import com.movem.backend.Dto.request.TaskRequests.Create.CreateTaskReminderRequest;
import com.movem.backend.Dto.request.TaskRequests.Update.UpdateTaskReminderRequest;
import com.movem.backend.Dto.response.ReminderResponses.UpcomingReminderResponse;
import com.movem.backend.Dto.response.TaskResponses.TaskReminderResponse;
import com.movem.backend.Entity.Tasks.Task;
import com.movem.backend.Entity.Tasks.TaskReminder;
import com.movem.backend.Entity.Auth.User;
import com.movem.backend.Exception.ResourceNotFoundException;
import com.movem.backend.Mapper.TaskMapper.TaskReminderMapper;
import com.movem.backend.model.enums.Activity.ActivityType;
import com.movem.backend.model.enums.ReminderType;
import com.movem.backend.Repository.TaskRepositories.TaskReminderRepository;
import com.movem.backend.Repository.TaskRepositories.TaskRepository;
import com.movem.backend.Service.AuthServices.CurrentUserService;
import com.movem.backend.Service.TaskServices.TaskReminderService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TaskReminderServiceImpl implements TaskReminderService {

    private final TaskRepository taskRepository;
    private final TaskReminderMapper taskReminderMapper;
    private final TaskReminderRepository taskReminderRepository;
    private final CurrentUserService currentUserService;

    @Override
    public void createReminders(
            Task task,
            List<CreateTaskReminderRequest> reminders
    ) {

        if (reminders == null || reminders.isEmpty()) {
            return;
        }

        List<TaskReminder> reminderList = new ArrayList<>();

        for (CreateTaskReminderRequest request : reminders) {

            TaskReminder reminder = new TaskReminder();

            reminder.setTask(task);

            reminder.setType(request.getType());


            if (request.getType() == ReminderType.DUE_DATE) {

                if (task.getActivity().getDeadline() == null) {
                    throw new IllegalArgumentException(
                            "Task deadline is required when using a DUE_DATE reminder."
                    );
                }

                reminder.setRemindAt(task.getActivity().getDeadline());
            } else {

                reminder.setRemindAt(
                        request.getRemindAt()
                );
            }

            reminder.setIsSent(false);

            reminder.setCreatedAt(LocalDateTime.now());

            reminderList.add(reminder);
        }

        taskReminderRepository.saveAll(reminderList);
    }

    @Override
    @Transactional(readOnly = true)
    public List<UpcomingReminderResponse> getUpcomingReminders() {

        User currentUser = currentUserService.getCurrentUser();

        System.out.println("=================================");
        System.out.println("Current User: " + currentUser.getId());

        List<TaskReminder> reminders =
                taskReminderRepository.findAllByTaskActivityUser(currentUser);

        System.out.println("Total reminders found = " + reminders.size());

        for (TaskReminder reminder : reminders) {

            System.out.println(
                    reminder.getId()
                            + " | "
                            + reminder.getRemindAt()
                            + " | "
                            + reminder.getType()
            );
        }

        LocalDateTime now = LocalDate.now().atStartOfDay();

        LocalDateTime nextWeek =
                now.plusDays(7)
                        .withHour(23)
                        .withMinute(59)
                        .withSecond(59)
                        .withNano(999999999);

        System.out.println("Now = " + now);
        System.out.println("Next Week = " + nextWeek);


        List<TaskReminder> upcoming =
                taskReminderRepository
                        .findByTaskActivityUserAndRemindAtAfterAndRemindAtBeforeOrderByRemindAtAsc(
                                currentUser,
                                now,
                                nextWeek
                        );
        System.out.println("Upcoming reminders = " + upcoming.size());

        for (TaskReminder reminder : upcoming) {
            System.out.println(
                    reminder.getId()
                            + " | "
                            + reminder.getRemindAt()
                            + " | "
                            + reminder.getType()
            );
        }

        return upcoming.stream()
                .map(reminder ->
                        UpcomingReminderResponse.builder()
                                .activityId(reminder.getTask().getActivity().getId())
                                .activityName(reminder.getTask().getActivity().getActivityName())
                                .activityType(ActivityType.TASK)
                                .remindAt(reminder.getRemindAt())
                                .reminderType(reminder.getType())
                                .build()
                )
                .toList();
    }

    @Override
    public List<TaskReminderResponse> getTaskReminders(String activityId) {
        Task task = taskRepository.findByActivityId(activityId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Task not found."));

        return taskReminderRepository.findByTaskOrderByRemindAtAsc(task)
                .stream()
                .map(taskReminderMapper::toResponse)
                .toList();
    }

    @Override
    public void addReminder(String activityId, CreateTaskReminderRequest request) {
        Task task = taskRepository.findByActivityId(activityId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Task not found."));

        TaskReminder reminder = new TaskReminder();

        reminder.setTask(task);
        reminder.setType(request.getType());
        reminder.setRemindAt(request.getRemindAt());
        reminder.setCreatedAt(LocalDateTime.now());
        reminder.setIsSent(false);

        taskReminderRepository.save(reminder);
    }

    @Override
    public void updateReminder(Integer reminderId, UpdateTaskReminderRequest request) {
        TaskReminder reminder = taskReminderRepository.findById(reminderId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Reminder not found."));

        reminder.setType(request.getType());
        reminder.setRemindAt(request.getRemindAt());

        reminder.setIsSent(false);

        taskReminderRepository.save(reminder);
    }

    @Override
    public void deleteReminder(Integer reminderId) {
        TaskReminder reminder = taskReminderRepository.findById(reminderId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Reminder not found."));

        taskReminderRepository.delete(reminder);
    }

    @Override
    public void syncDueDateReminders(Task task) {

        try {

            LocalDateTime deadline = task.getActivity().getDeadline();

            List<TaskReminder> reminders = taskReminderRepository.findByTask(task);

            for (TaskReminder reminder : reminders) {


                if (reminder.getType() == ReminderType.DUE_DATE) {
                    reminder.setRemindAt(deadline);
                }
            }

            taskReminderRepository.saveAll(reminders);

        } catch (Exception e) {

            e.printStackTrace();

            throw e;
        }
    }

}
package com.movem.backend.shared.reminder.services;

import com.movem.backend.shared.reminder.dtos.requests.CreateReminderRequest;
import com.movem.backend.shared.reminder.dtos.requests.UpdateReminderRequest;
import com.movem.backend.shared.reminder.dtos.responses.UpcomingReminderResponse;
import com.movem.backend.shared.reminder.dtos.responses.ReminderResponse;
import com.movem.backend.task.entities.Task;

import java.util.List;

public interface ReminderService {
    void createReminders(Task task, List<CreateReminderRequest> reminders);
    void syncTaskReminders(Task task);
    List<UpcomingReminderResponse> getUpcomingReminders();
    List<ReminderResponse> getTaskReminders(String activityId);
    void addReminder(String activityId, CreateReminderRequest request);
    void updateReminder(Integer reminderId, UpdateReminderRequest request);
    void deleteReminder(Integer reminderId);
    void processDueReminders();
    void addCustomReminders(Task task, List<CreateReminderRequest> reminders);

}
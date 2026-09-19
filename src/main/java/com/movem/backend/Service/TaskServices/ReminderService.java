package com.movem.backend.Service.TaskServices;

import com.movem.backend.Dto.request.TaskRequests.Create.CreateTaskReminderRequest;
import com.movem.backend.Dto.request.TaskRequests.Update.UpdateTaskReminderRequest;
import com.movem.backend.Dto.response.ReminderResponses.UpcomingReminderResponse;
import com.movem.backend.Dto.response.TaskResponses.ReminderResponse;
import com.movem.backend.Entity.Tasks.Task;

import java.util.List;

public interface ReminderService {
    void createReminders(Task task, List<CreateTaskReminderRequest> reminders);
    void syncTaskReminders(Task task);
    List<UpcomingReminderResponse> getUpcomingReminders();
    List<ReminderResponse> getTaskReminders(String activityId);
    void addReminder(String activityId, CreateTaskReminderRequest request);
    void updateReminder(Integer reminderId, UpdateTaskReminderRequest request);
    void deleteReminder(Integer reminderId);
    void processDueReminders();
    void addCustomReminders(Task task, List<CreateTaskReminderRequest> reminders);

}
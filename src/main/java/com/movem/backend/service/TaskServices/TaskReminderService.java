package com.movem.backend.service.TaskServices;

import com.movem.backend.dto.request.TaskRequests.Create.CreateTaskReminderRequest;
import com.movem.backend.dto.response.ReminderResponses.UpcomingReminderResponse;
import com.movem.backend.entity.Tasks.Task;

import java.util.List;

public interface TaskReminderService {

    void createReminders(
            Task task,
            List<CreateTaskReminderRequest> reminders
    );

    void syncDueDateReminders(Task task);

    List<UpcomingReminderResponse> getUpcomingReminders();

}
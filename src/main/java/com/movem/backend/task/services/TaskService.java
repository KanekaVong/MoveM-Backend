package com.movem.backend.task.services;

import com.movem.backend.task.dtos.requests.Create.CreateTaskRequest;
import com.movem.backend.task.dtos.requests.Update.UpdateTaskRequest;
import com.movem.backend.task.dtos.responses.TaskResponse;
import com.movem.backend.commons.enums.shared.ActivityStatus;
import com.movem.backend.commons.enums.Task.Priority;
import java.util.List;


public interface TaskService {

    TaskResponse getTask(String activityId);

    TaskResponse createTask(CreateTaskRequest request);

    TaskResponse updateTask(
            String activityId,
            UpdateTaskRequest request
    );

    List<TaskResponse> getMyTasks();

    void deleteTask(String activityId);

    TaskResponse restoreTask(String activityId);

    List<TaskResponse> searchTasks(
            String search,
            ActivityStatus status,
            Priority priority,
            Integer labelId,
            String sortBy,
            String direction,
            Boolean overdue,
            Integer upcomingDays,
            Boolean active
    );

    TaskResponse completeTask(String activityId);

}
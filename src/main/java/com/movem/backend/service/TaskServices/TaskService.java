package com.movem.backend.service.TaskServices;

import com.movem.backend.dto.request.TaskRequests.Create.CreateTaskRequest;
import com.movem.backend.dto.request.TaskRequests.Update.UpdateTaskRequest;
import com.movem.backend.dto.response.TaskResponses.TaskResponse;
import com.movem.backend.model.enums.Activity.ActivityStatus;
import com.movem.backend.model.enums.Priority;

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
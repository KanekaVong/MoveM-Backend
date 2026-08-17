package com.movem.backend.Service.TaskServices;

import com.movem.backend.Dto.request.TaskRequests.Create.CreateChecklistItemRequest;
import com.movem.backend.Dto.request.TaskRequests.Update.UpdateChecklistItemRequest;
import com.movem.backend.Dto.response.TaskResponses.TaskChecklistResponse;
import com.movem.backend.Entity.Tasks.Task;

import java.util.List;

public interface TaskChecklistService {

    void createChecklistItems(
            Task task,
            List<CreateChecklistItemRequest> items
    );

    List<TaskChecklistResponse> getChecklistItems(
            String activityId
    );

    void markChecklistCompleted(Integer id);

    void addChecklistItem(
            String activityId,
            CreateChecklistItemRequest request
    );

    void updateChecklistItem(
            Integer checklistId,
            UpdateChecklistItemRequest request
    );

    void toggleChecklistCompletion(
            Integer checklistId
    );

    void deleteChecklistItem(
            Integer checklistId
    );
}
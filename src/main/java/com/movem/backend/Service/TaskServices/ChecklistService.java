package com.movem.backend.Service.TaskServices;

import com.movem.backend.Dto.request.TaskRequests.Create.CreateChecklistItemRequest;
import com.movem.backend.Dto.request.TaskRequests.Update.UpdateChecklistItemRequest;
import com.movem.backend.Dto.response.TaskResponses.ChecklistResponse;
import com.movem.backend.Entity.Tasks.Task;
import com.movem.backend.Entity.Trip.Trip;

import java.util.List;

public interface ChecklistService {

    void createChecklistItems(Task task, List<CreateChecklistItemRequest> items);
    void createTripChecklistItems(Trip trip, List<CreateChecklistItemRequest> items);

    List<ChecklistResponse> getChecklistItems(String activityId);
    List<ChecklistResponse> getTripChecklistItems(String activityId);

    void markChecklistCompleted(Integer id);
    void addChecklistItem(String activityId, CreateChecklistItemRequest request);
    void updateChecklistItem(Integer checklistId, UpdateChecklistItemRequest request);
    void updateTripChecklistItem(String activityId, Integer checklistId, UpdateChecklistItemRequest request);
    void toggleChecklistCompletion(Integer checklistId);
    void deleteChecklistItem(Integer checklistId);
}
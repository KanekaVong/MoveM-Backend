package com.movem.backend.shared.checklist.services;

import com.movem.backend.task.dtos.requests.Create.CreateChecklistItemRequest;
import com.movem.backend.shared.checklist.dtos.requests.UpdateChecklistItemRequest;
import com.movem.backend.shared.checklist.dtos.responses.ChecklistResponse;
import com.movem.backend.task.entities.Task;
import com.movem.backend.trip.entities.Trip;

import java.util.List;

public interface ChecklistService {

    void createChecklistItems(Task task, List<CreateChecklistItemRequest> items);
    void createTripChecklistItems(Trip trip, List<CreateChecklistItemRequest> items);

    List<ChecklistResponse> getChecklistItems(String activityId);

    List<ChecklistResponse> getTripChecklistItems(String activityId);

    void markChecklistCompleted(Integer id);
    void addChecklistItem(String activityId, CreateChecklistItemRequest request);
    void updateChecklistItems(Task task, List<UpdateChecklistItemRequest> requests);
    void updateTripChecklistItem(String activityId, Integer checklistId, UpdateChecklistItemRequest request);
    void toggleChecklistCompletion(Integer checklistId);
    void toggleTripChecklistCompletion(String activityId, Integer checklistId);
    void deleteChecklistItem(Integer checklistId);
}
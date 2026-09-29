package com.movem.backend.shared.checklist.services;

import com.movem.backend.task.dtos.requests.Create.CreateChecklistItemRequest;
import com.movem.backend.shared.checklist.dtos.requests.UpdateChecklistItemRequest;
import com.movem.backend.shared.checklist.dtos.responses.ChecklistResponse;
import com.movem.backend.task.entities.Task;
import com.movem.backend.trip.entities.Trip;

import java.util.List;

public interface ChecklistService {
    List<ChecklistResponse> getChecklistItems(String activityId);

    void addChecklistItem(String activityId, CreateChecklistItemRequest request);

    void updateChecklistItem(String activityId, Integer checklistId, UpdateChecklistItemRequest request);

    void toggleChecklistCompletion(String activityId, Integer checklistId);

    void deleteChecklistItem(String activityId, Integer checklistId);
}
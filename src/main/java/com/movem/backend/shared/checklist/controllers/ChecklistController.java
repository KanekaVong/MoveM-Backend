package com.movem.backend.shared.checklist.controllers;

import com.movem.backend.task.dtos.requests.Create.CreateChecklistItemRequest;
import com.movem.backend.shared.checklist.dtos.requests.UpdateChecklistItemRequest;
import com.movem.backend.shared.checklist.dtos.responses.ChecklistResponse;
import com.movem.backend.task.entities.Task;
import com.movem.backend.shared.checklist.services.ChecklistService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/shared")
@Tag(name = "Task - Checklists")
@RequiredArgsConstructor
public class ChecklistController {

    private final ChecklistService checklistService;

    @GetMapping("/{activityId}/checklists")
    public ResponseEntity<List<ChecklistResponse>> getChecklistItems(@PathVariable String activityId) {
        return ResponseEntity.ok(checklistService.getChecklistItems(activityId));
    }

    @PostMapping("/{activityId}/checklists")
    public ResponseEntity<Void> addChecklistItem(@PathVariable String activityId, @Valid @RequestBody CreateChecklistItemRequest request) {
        checklistService.addChecklistItem(activityId, request);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PutMapping("/{checklistId}/checklists")
    public ResponseEntity<Void> updateChecklistItem(@PathVariable Task task, @Valid @RequestBody List<UpdateChecklistItemRequest> requests) {
        checklistService.updateChecklistItems(task, requests);
        return ResponseEntity.noContent().build();
    }


    @PatchMapping("/checklists/{checklistId}/complete")
    public ResponseEntity<Void> toggleChecklistCompletion(@PathVariable Integer checklistId) {
        checklistService.toggleChecklistCompletion(checklistId);
        return ResponseEntity.noContent().build();
    }
    @DeleteMapping("/checklists/{checklistId}")
    public ResponseEntity<Void> deleteChecklistItem(@PathVariable Integer checklistId ) {
        checklistService.deleteChecklistItem(checklistId);
        return ResponseEntity.noContent().build();
    }

}
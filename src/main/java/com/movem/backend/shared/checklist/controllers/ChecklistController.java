package com.movem.backend.shared.checklist.controllers;

import com.movem.backend.task.dtos.requests.Create.CreateChecklistItemRequest;
import com.movem.backend.shared.checklist.dtos.requests.UpdateChecklistItemRequest;
import com.movem.backend.shared.checklist.dtos.responses.ChecklistResponse;
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
@Tag(name = "Shared - Checklists")
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

    @PutMapping("/{activityId}/checklists/{checklistId}")
    public ResponseEntity<Void> updateChecklistItem(@PathVariable String activityId, @PathVariable Integer checklistId, @Valid @RequestBody UpdateChecklistItemRequest request) {
        checklistService.updateChecklistItem(activityId, checklistId, request);

        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{activityId}/checklists/{checklistId}/complete")
    public ResponseEntity<Void> toggleChecklistCompletion(@PathVariable String activityId, @PathVariable Integer checklistId) {
        checklistService.toggleChecklistCompletion(activityId, checklistId);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{activityId}/checklists/{checklistId}")
    public ResponseEntity<Void> deleteChecklistItem(@PathVariable String activityId, @PathVariable Integer checklistId) {
        checklistService.deleteChecklistItem(activityId, checklistId);
        return ResponseEntity.noContent().build();
    }
}
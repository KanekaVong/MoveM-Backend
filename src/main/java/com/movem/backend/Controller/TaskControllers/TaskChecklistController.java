package com.movem.backend.Controller.TaskControllers;

import com.movem.backend.Dto.request.TaskRequests.Create.CreateChecklistItemRequest;
import com.movem.backend.Dto.request.TaskRequests.Update.UpdateChecklistItemRequest;
import com.movem.backend.Dto.response.TaskResponses.ChecklistResponse;
import com.movem.backend.Service.TaskServices.ChecklistService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/tasks")
@Tag(
        name = "Task - Checklists"
)
@RequiredArgsConstructor
public class TaskChecklistController {

    private final ChecklistService checklistService;

    @GetMapping("/{activityId}/checklists")
    public ResponseEntity<List<ChecklistResponse>> getChecklistItems(
            @PathVariable String activityId
    ) {

        return ResponseEntity.ok(
                checklistService.getChecklistItems(activityId)
        );

    }

    @PostMapping("/{activityId}/checklists")
    public ResponseEntity<Void> addChecklistItem(
            @PathVariable String activityId,
            @Valid @RequestBody CreateChecklistItemRequest request
    ) {

        checklistService.addChecklistItem(
                activityId,
                request
        );

        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PutMapping("/{checklistId}/checklists")
    public ResponseEntity<Void> updateChecklistItem(
            @PathVariable Integer checklistId,
            @Valid @RequestBody UpdateChecklistItemRequest request
    ) {

        checklistService.updateChecklistItem(
                checklistId,
                request
        );

        return ResponseEntity.noContent().build();
    }


    @PatchMapping("/checklists/{checklistId}/complete")
    public ResponseEntity<Void> toggleChecklistCompletion(
            @PathVariable Integer checklistId
    ) {

        checklistService.toggleChecklistCompletion(checklistId);

        return ResponseEntity.noContent().build();
    }
    @DeleteMapping("/checklists/{checklistId}")
    public ResponseEntity<Void> deleteChecklistItem(
            @PathVariable Integer checklistId
    ) {

        checklistService.deleteChecklistItem(checklistId);

        return ResponseEntity.noContent().build();
    }

}
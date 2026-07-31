package com.movem.backend.controller.TaskControllers;

import com.movem.backend.service.TaskServices.TaskChecklistService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/tasks/checklists")
@RequiredArgsConstructor
public class TaskChecklistController {

    private final TaskChecklistService taskChecklistService;

    @PatchMapping("/{id}/complete")
    public ResponseEntity<Void> completeChecklist(
            @PathVariable Integer id
    ) {

        taskChecklistService.markChecklistCompleted(id);

        return ResponseEntity.noContent().build();
    }

}
package com.movem.backend.Controller.TaskControllers;

import com.movem.backend.Dto.request.TaskRequests.Create.CreateTaskRequest;
import com.movem.backend.Dto.request.TaskRequests.Update.UpdateTaskRequest;
import com.movem.backend.Dto.response.TaskResponses.TaskResponse;
import com.movem.backend.model.enums.Activity.ActivityStatus;
import com.movem.backend.model.enums.Priority;
import com.movem.backend.Service.TaskServices.TaskService;
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
        name = "Task"
)
@RequiredArgsConstructor
public class TaskController {

    private final TaskService taskService;

    @PostMapping
    public ResponseEntity<TaskResponse> createTask(
            @Valid @RequestBody CreateTaskRequest request
    ) {

        TaskResponse response = taskService.createTask(request);

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/{activityId}")
    public ResponseEntity<TaskResponse> getTask(
            @PathVariable String activityId
    ) {
        return ResponseEntity.ok(
                taskService.getTask(activityId)
        );
    }

    @GetMapping
    public ResponseEntity<List<TaskResponse>> getMyTasks(
            @RequestParam(required = false) String search,
            @RequestParam(required = false) ActivityStatus status,
            @RequestParam(required = false) Priority priority,
            @RequestParam(required = false) Integer labelId,
            @RequestParam(required = false) String sortBy,
            @RequestParam(required = false, defaultValue = "asc") String direction,
            @RequestParam(required = false) Boolean overdue,
            @RequestParam(required = false) Integer upcomingDays,
            @RequestParam(required = false) Boolean active

    ) {
            return ResponseEntity.ok(
                    taskService.searchTasks(
                            search,
                            status,
                            priority,
                            labelId,
                            sortBy,
                            direction,
                            overdue,
                            upcomingDays,
                            active
                    )
            );
    }

    @PutMapping("/{activityId}")
    public ResponseEntity<TaskResponse> updateTask(
            @PathVariable String activityId,
            @RequestBody @Valid UpdateTaskRequest request
    ) {
        return ResponseEntity.ok(taskService.updateTask(activityId, request));
    }

    @DeleteMapping("/{activityId}")
    public ResponseEntity<Void> deleteTask(
            @PathVariable String activityId
    ) {

        taskService.deleteTask(activityId);

        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{activityId}/restore")
    public ResponseEntity<TaskResponse> restoreTask(
            @PathVariable String activityId
    ) {
        return ResponseEntity.ok(
                taskService.restoreTask(activityId)
        );
    }

    @PatchMapping("/{activityId}/complete")
    public ResponseEntity<TaskResponse> completeTask(
            @PathVariable String activityId
    ) {

        return ResponseEntity.ok(
                taskService.completeTask(activityId)
        );
    }
}

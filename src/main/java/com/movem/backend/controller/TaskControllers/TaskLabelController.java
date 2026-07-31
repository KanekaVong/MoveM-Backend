package com.movem.backend.controller.TaskControllers;

import com.movem.backend.dto.request.TaskRequests.Create.CreateTaskLabelRequest;
import com.movem.backend.dto.request.TaskRequests.Update.UpdateTaskLabelRequest;
import com.movem.backend.dto.response.TaskResponses.TaskLabelResponse;
import com.movem.backend.service.TaskServices.TaskLabelService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/task-labels")
@RequiredArgsConstructor
public class TaskLabelController {

    private final TaskLabelService taskLabelService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public TaskLabelResponse create(@Valid @RequestBody CreateTaskLabelRequest request) {
        return taskLabelService.create(request);
    }

    @GetMapping
    public List<TaskLabelResponse> getMyLabels() {
        return taskLabelService.getMyLabels();
    }

    @PutMapping("/{id}")
    public TaskLabelResponse update(
            @PathVariable Integer id,
            @Valid @RequestBody UpdateTaskLabelRequest request) {

        return taskLabelService.update(id, request);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Integer id) {
        taskLabelService.delete(id);
    }
}

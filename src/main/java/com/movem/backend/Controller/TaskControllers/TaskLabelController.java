package com.movem.backend.Controller.TaskControllers;

import com.movem.backend.Dto.request.TaskRequests.Create.CreateTaskLabelRequest;
import com.movem.backend.Dto.request.TaskRequests.Update.UpdateTaskLabelRequest;
import com.movem.backend.Dto.response.TaskResponses.TaskLabelResponse;
import com.movem.backend.Service.TaskServices.TaskLabelService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/task-labels")
@Tag(
        name = "Task - Labels"
)
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

package com.movem.backend.Controller.TaskControllers;

import com.movem.backend.Dto.request.TaskRequests.Create.CreateTaskReminderRequest;
import com.movem.backend.Dto.request.TaskRequests.Update.UpdateTaskReminderRequest;
import com.movem.backend.Dto.response.ReminderResponses.UpcomingReminderResponse;
import com.movem.backend.Dto.response.TaskResponses.TaskReminderResponse;
import com.movem.backend.Service.TaskServices.TaskReminderService;
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
        name = "Task - Reminders"
)
@RequiredArgsConstructor
public class TaskReminderController {

    private final TaskReminderService taskReminderService;

    // Existing
    @GetMapping("/reminders/upcoming")
    public ResponseEntity<List<UpcomingReminderResponse>> getUpcomingReminders() {

        return ResponseEntity.ok(
                taskReminderService.getUpcomingReminders()
        );
    }

    // Get reminders for one task
    @GetMapping("/{activityId}/reminders")
    public ResponseEntity<List<TaskReminderResponse>> getTaskReminders(
            @PathVariable String activityId
    ) {

        return ResponseEntity.ok(
                taskReminderService.getTaskReminders(activityId)
        );
    }

    // Add reminder
    @PostMapping("/{activityId}/reminders")
    public ResponseEntity<Void> addReminder(
            @PathVariable String activityId,
            @Valid @RequestBody CreateTaskReminderRequest request
    ) {

        taskReminderService.addReminder(activityId, request);

        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    // Update reminder
    @PutMapping("/reminders/{reminderId}")
    public ResponseEntity<Void> updateReminder(
            @PathVariable Integer reminderId,
            @Valid @RequestBody UpdateTaskReminderRequest request
    ) {

        taskReminderService.updateReminder(reminderId, request);

        return ResponseEntity.noContent().build();
    }

    // Delete reminder
    @DeleteMapping("/reminders/{reminderId}")
    public ResponseEntity<Void> deleteReminder(
            @PathVariable Integer reminderId
    ) {

        taskReminderService.deleteReminder(reminderId);

        return ResponseEntity.noContent().build();
    }

}
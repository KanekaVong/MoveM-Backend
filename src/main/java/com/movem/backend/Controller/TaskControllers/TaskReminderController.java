package com.movem.backend.Controller.TaskControllers;

import com.movem.backend.Dto.request.TaskRequests.Create.CreateTaskReminderRequest;
import com.movem.backend.Dto.request.TaskRequests.Update.UpdateTaskReminderRequest;
import com.movem.backend.Dto.response.ReminderResponses.UpcomingReminderResponse;
import com.movem.backend.Dto.response.TaskResponses.ReminderResponse;
import com.movem.backend.Service.TaskServices.ReminderService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/tasks")
@Tag(name = "Task - Reminders")
@RequiredArgsConstructor
public class TaskReminderController {
    private final ReminderService reminderService;

    @GetMapping("/reminders/upcoming")
    public ResponseEntity<List<UpcomingReminderResponse>> getUpcomingReminders() {
        return ResponseEntity.ok(reminderService.getUpcomingReminders());
    }

    @GetMapping("/{activityId}/reminders")
    public ResponseEntity<List<ReminderResponse>> getTaskReminders(@PathVariable String activityId) {
        return ResponseEntity.ok(reminderService.getTaskReminders(activityId));
    }

    @PostMapping("/{activityId}/reminders")
    public ResponseEntity<Void> addReminder(@PathVariable String activityId, @Valid @RequestBody CreateTaskReminderRequest request) {
        reminderService.addReminder(activityId, request);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PutMapping("/reminders/{reminderId}")
    public ResponseEntity<Void> updateReminder(@PathVariable Integer reminderId, @Valid @RequestBody UpdateTaskReminderRequest request) {
        reminderService.updateReminder(reminderId, request);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/reminders/{reminderId}")
    public ResponseEntity<Void> deleteReminder(@PathVariable Integer reminderId) {
        reminderService.deleteReminder(reminderId);
        return ResponseEntity.noContent().build();
    }
}
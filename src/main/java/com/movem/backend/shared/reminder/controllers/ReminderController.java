package com.movem.backend.shared.reminder.controllers;

import com.movem.backend.shared.reminder.dtos.requests.CreateReminderRequest;
import com.movem.backend.shared.reminder.dtos.requests.UpdateReminderRequest;
import com.movem.backend.shared.reminder.dtos.responses.UpcomingReminderResponse;
import com.movem.backend.shared.reminder.dtos.responses.ReminderResponse;
import com.movem.backend.shared.reminder.services.ReminderService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/shared")
@Tag(name = "Task - Reminders")
@RequiredArgsConstructor
public class ReminderController {
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
    public ResponseEntity<Void> addReminder(@PathVariable String activityId, @Valid @RequestBody CreateReminderRequest request) {
        reminderService.addReminder(activityId, request);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PutMapping("/reminders/{reminderId}")
    public ResponseEntity<Void> updateReminder(@PathVariable Integer reminderId, @Valid @RequestBody UpdateReminderRequest request) {
        reminderService.updateReminder(reminderId, request);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/reminders/{reminderId}")
    public ResponseEntity<Void> deleteReminder(@PathVariable Integer reminderId) {
        reminderService.deleteReminder(reminderId);
        return ResponseEntity.noContent().build();
    }
}
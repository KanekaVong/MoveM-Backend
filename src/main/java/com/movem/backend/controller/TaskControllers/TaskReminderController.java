package com.movem.backend.controller.TaskControllers;

import com.movem.backend.dto.response.ReminderResponses.UpcomingReminderResponse;
import com.movem.backend.service.TaskServices.TaskReminderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/tasks/reminders")
@RequiredArgsConstructor
public class TaskReminderController {

    private final TaskReminderService taskReminderService;

    @GetMapping("/upcoming")
    public ResponseEntity<List<UpcomingReminderResponse>>
    getUpcomingReminders() {

        return ResponseEntity.ok(
                taskReminderService.getUpcomingReminders()
        );
    }
}

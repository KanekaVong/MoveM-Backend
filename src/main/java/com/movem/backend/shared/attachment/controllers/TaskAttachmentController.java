package com.movem.backend.shared.attachment.controllers;

import com.movem.backend.shared.attachment.dtos.responses.AttachmentResponse;
import com.movem.backend.shared.attachment.services.TaskAttachmentService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/tasks")
@Tag(name = "Task - Attachments", description = "Add Attachment to tasks")
@RequiredArgsConstructor
public class TaskAttachmentController {
    private final TaskAttachmentService taskAttachmentService;

    @PostMapping(value = "/{activityId}/attachments", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<AttachmentResponse> uploadAttachment(@PathVariable String activityId, @RequestParam("file") MultipartFile file) {
        return ResponseEntity.ok(taskAttachmentService.upload(activityId, file));
    }

    @GetMapping("/{activityId}/attachments")
    public ResponseEntity<List<AttachmentResponse>> getAttachments(@PathVariable String activityId) {
        return ResponseEntity.ok(taskAttachmentService.getAttachments(activityId));
    }
}
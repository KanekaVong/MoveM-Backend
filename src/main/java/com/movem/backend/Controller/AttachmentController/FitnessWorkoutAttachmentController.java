package com.movem.backend.Controller.AttachmentController;

import com.movem.backend.Dto.response.Attachment.AttachmentResponse;
import com.movem.backend.Service.AttachmentService.FitnessWorkoutAttachmentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/workouts")
@RequiredArgsConstructor
public class FitnessWorkoutAttachmentController {
    private final FitnessWorkoutAttachmentService workoutAttachmentService;

    @PostMapping(value = "/{sessionId}/attachments", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<AttachmentResponse> uploadAttachment(@PathVariable Integer sessionId, @RequestParam("file") MultipartFile file) {
        return ResponseEntity.ok(workoutAttachmentService.upload(sessionId, file));
    }

    @GetMapping("/{sessionId}/attachments")
    public ResponseEntity<List<AttachmentResponse>> getWorkoutAttachments(@PathVariable Integer sessionId) {
        return ResponseEntity.ok(workoutAttachmentService.getAttachments(sessionId));
    }
}
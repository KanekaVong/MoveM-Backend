package com.movem.backend.shared.attachment.controllers;

import com.movem.backend.shared.attachment.dtos.responses.AttachmentResponse;
import com.movem.backend.shared.attachment.services.AttachmentService;
import com.movem.backend.shared.attachment.services.FitnessWorkoutAttachmentService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/workouts")
@Tag(name = "Fitness Attachments", description = "Upload, Fetch Picture URLs, Download, etc.")
@RequiredArgsConstructor
public class FitnessWorkoutAttachmentController {
    private final FitnessWorkoutAttachmentService workoutAttachmentService;
    private final AttachmentService attachmentService;

    @PostMapping(value = "/{sessionId}/attachments", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<AttachmentResponse> uploadWorkoutAttachment(@PathVariable Integer sessionId, @RequestParam("file") MultipartFile file) {
        return ResponseEntity.ok(workoutAttachmentService.upload(sessionId, file));
    }
    @GetMapping("/{sessionId}/attachments")
    public ResponseEntity<List<AttachmentResponse>> getWorkoutAttachments(@PathVariable Integer sessionId) {
        return ResponseEntity.ok(workoutAttachmentService.getAttachments(sessionId));
    }
    @PostMapping(value = "/clubs/{clubId}/profile", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<AttachmentResponse> uploadFitnessClubProfile(@PathVariable Integer clubId, @RequestParam("file") MultipartFile file) {
        return ResponseEntity.status(HttpStatus.CREATED).body(attachmentService.uploadClubProfile(clubId, file));
    }
    @PostMapping(value = "/clubs/{clubId}/cover", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<AttachmentResponse> uploadFitnessClubCover(@PathVariable Integer clubId, @RequestParam("file") MultipartFile file) {
        return ResponseEntity.status(HttpStatus.CREATED).body(attachmentService.uploadClubCover(clubId, file));
    }
    @GetMapping("/clubs/{clubId}")
    public ResponseEntity<List<AttachmentResponse>> getClubAttachments(@PathVariable Integer clubId) {
        return ResponseEntity.ok(attachmentService.getClubAttachments(clubId));
    }
    @PostMapping(value = "/challenges/{challengeId}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<AttachmentResponse> uploadChallengeAttachment(@PathVariable Integer challengeId, @RequestParam("file") MultipartFile file) {
        return ResponseEntity.status(HttpStatus.CREATED).body(attachmentService.uploadChallengeAttachment(challengeId, file));
    }
    @GetMapping("/challenges/{challengeId}")
    public ResponseEntity<List<AttachmentResponse>> getChallengeAttachments(@PathVariable Integer challengeId) {
        return ResponseEntity.ok(attachmentService.getChallengeAttachments(challengeId));
    }

}
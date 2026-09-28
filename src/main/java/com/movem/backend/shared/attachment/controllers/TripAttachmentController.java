package com.movem.backend.shared.attachment.controllers;

import com.movem.backend.shared.attachment.dtos.responses.AttachmentResponse;
import com.movem.backend.shared.attachment.services.TripAttachmentService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/trips")
@Tag(name = "Trip Attachments", description = "Upload, Fetch Picture URLs, Download, etc.")
@RequiredArgsConstructor
public class TripAttachmentController {

    private final TripAttachmentService tripAttachmentService;

    @PostMapping(value = "/{activityId}/attachments", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<AttachmentResponse> uploadTripAttachment(@PathVariable String activityId, @RequestParam("file") MultipartFile file) {
        return ResponseEntity.ok(tripAttachmentService.upload(activityId, file));
    }

    @GetMapping("/{activityId}/attachments")
    public ResponseEntity<List<AttachmentResponse>> getTripAttachments(@PathVariable String activityId) {
        return ResponseEntity.ok(tripAttachmentService.getAttachments(activityId));
    }

    @PostMapping(value = "/{activityId}/cover-photo", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<AttachmentResponse> uploadTripCoverPhoto(@PathVariable String activityId, @RequestParam("file") MultipartFile file) {
        return ResponseEntity.ok(tripAttachmentService.uploadCoverPhoto(activityId, file));
    }

    @GetMapping("/{activityId}/cover-photo")
    public ResponseEntity<AttachmentResponse> getCoverPhoto(@PathVariable String activityId) {
        return ResponseEntity.ok(tripAttachmentService.getCoverPhoto(activityId));
    }
}
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
@Tag(name = "Trip - Attachments", description = "Add Attachment to trips")
@RequiredArgsConstructor
public class TripAttachmentController {

    private final TripAttachmentService tripAttachmentService;

    @PostMapping(
            value = "/{activityId}/attachments",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE
    )
    public ResponseEntity<AttachmentResponse> uploadAttachment(
            @PathVariable String activityId,
            @RequestParam("file") MultipartFile file
    ) {
        return ResponseEntity.ok(
                tripAttachmentService.upload(activityId, file)
        );
    }

    @GetMapping("/{activityId}/attachments")
    public ResponseEntity<List<AttachmentResponse>> getAttachments(@PathVariable String activityId) {
        return ResponseEntity.ok(tripAttachmentService.getAttachments(activityId));
    }

    @PostMapping(
            value = "/{activityId}/cover-photo",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE
    )
    public ResponseEntity<AttachmentResponse> uploadCoverPhoto(
            @PathVariable String activityId,
            @RequestParam("file") MultipartFile file
    ) {
        return ResponseEntity.ok(
                tripAttachmentService.uploadCoverPhoto(activityId, file)
        );
    }

    @GetMapping("/{activityId}/cover-photo")
    public ResponseEntity<AttachmentResponse> getCoverPhoto(
            @PathVariable String activityId
    ) {
        return ResponseEntity.ok(
                tripAttachmentService.getCoverPhoto(activityId)
        );
    }

    @DeleteMapping("/{activityId}/cover-photo")
    public ResponseEntity<Void> deleteCoverPhoto(
            @PathVariable String activityId
    ) {
        tripAttachmentService.deleteCoverPhoto(activityId);
        return ResponseEntity.noContent().build();
    }
}
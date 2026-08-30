package com.movem.backend.Controller.AttachmentController;

import com.movem.backend.Dto.response.Attachment.AttachmentResponse;
import com.movem.backend.Service.AttachmentService.TripAttachmentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/trips")
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
                tripAttachmentService.upload(
                        activityId,
                        file
                )
        );
    }

    @GetMapping("/{activityId}/attachments")
    public ResponseEntity<List<AttachmentResponse>> getAttachments(
            @PathVariable String activityId
    ) {

        return ResponseEntity.ok(
                tripAttachmentService.getAttachments(activityId)
        );
    }
}
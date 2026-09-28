package com.movem.backend.shared.attachment.controllers;

import com.movem.backend.shared.attachment.dtos.responses.AttachmentResponse;
import com.movem.backend.shared.attachment.services.AttachmentService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/attachments")
@Tag(name = "Attachments", description = "Upload, Fetch Picture URLs, Download, etc.")
@RequiredArgsConstructor
public class AttachmentController {
    private final AttachmentService attachmentService;

    @GetMapping("/{attachmentId}/viewAttachments")
    public ResponseEntity<Resource> view(@PathVariable Long attachmentId) {
        return attachmentService.view(attachmentId);
    }

    @GetMapping("/{attachmentId}/downloadAttachments")
    public ResponseEntity<Resource> download(@PathVariable Long attachmentId) {
        return attachmentService.download(attachmentId);
    }

    @GetMapping
    public ResponseEntity<List<AttachmentResponse>> getMyAttachments() {
        return ResponseEntity.ok(attachmentService.getMyAttachments());
    }

    @DeleteMapping("/{attachmentId}/deleteAnyAttachment")
    public ResponseEntity<Void> deleteAnyAttachment(@PathVariable Long attachmentId) {
        attachmentService.delete(attachmentId);
        return ResponseEntity.noContent().build();
    }
}
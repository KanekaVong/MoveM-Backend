package com.movem.backend.Controller.AttachmentController;

import com.movem.backend.Dto.response.Attachment.AttachmentResponse;
import com.movem.backend.Service.AttachmentService.AttachmentService;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/attachments")
@RequiredArgsConstructor
public class AttachmentController {

    private final AttachmentService attachmentService;

    @PostMapping(
            value = "/upload",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE
    )
    public ResponseEntity<AttachmentResponse> upload(
            @RequestParam("file") MultipartFile file
    ) {

        return ResponseEntity.ok(
                attachmentService.upload(file)
        );
    }

    @GetMapping("/{attachmentId}/view")
    public ResponseEntity<Resource> view(
            @PathVariable Long attachmentId
    ) {

        return attachmentService.view(attachmentId);
    }

    @GetMapping("/{attachmentId}/download")
    public ResponseEntity<Resource> download(
            @PathVariable Long attachmentId
    ) {

        return attachmentService.download(attachmentId);
    }

    @GetMapping
    public ResponseEntity<List<AttachmentResponse>> getMyAttachments() {

        return ResponseEntity.ok(
                attachmentService.getMyAttachments()
        );
    }

    @GetMapping("/{attachmentId}")
    public ResponseEntity<AttachmentResponse> getAttachment(
            @PathVariable Long attachmentId
    ) {

        return ResponseEntity.ok(
                attachmentService.getAttachment(attachmentId)
        );
    }

    @DeleteMapping("/{attachmentId}")
    public ResponseEntity<Void> delete(
            @PathVariable Long attachmentId
    ) {

        attachmentService.delete(attachmentId);

        return ResponseEntity.noContent().build();
    }
}
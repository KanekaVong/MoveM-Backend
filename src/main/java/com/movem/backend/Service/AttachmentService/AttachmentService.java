package com.movem.backend.Service.AttachmentService;

import com.movem.backend.Dto.response.Attachment.AttachmentResponse;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface AttachmentService {

    AttachmentResponse upload(MultipartFile file);

    List<AttachmentResponse> getMyAttachments();

    AttachmentResponse getAttachment(Long attachmentId);

    void delete(Long attachmentId);

    ResponseEntity<Resource> view(Long attachmentId);

    ResponseEntity<Resource> download(Long attachmentId);
}
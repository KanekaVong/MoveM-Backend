package com.movem.backend.shared.attachment.services;

import com.movem.backend.shared.attachment.dtos.responses.AttachmentResponse;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface TaskAttachmentService {

    AttachmentResponse upload(String activityId, MultipartFile file);

    List<AttachmentResponse> getAttachments(String activityId);
}
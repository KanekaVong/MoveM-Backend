package com.movem.backend.Service.AttachmentService;

import com.movem.backend.Dto.response.Attachment.AttachmentResponse;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface TaskAttachmentService {

    AttachmentResponse upload(
            String activityId,
            MultipartFile file
    );

    List<AttachmentResponse> getAttachments(
            String activityId
    );
}
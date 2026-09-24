package com.movem.backend.shared.attachment.services;

import com.movem.backend.shared.attachment.dtos.responses.AttachmentResponse;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface TripAttachmentService {

    AttachmentResponse upload(String activityId, MultipartFile file);

    List<AttachmentResponse> getAttachments(String activityId);
    AttachmentResponse uploadCoverPhoto(String activityId, MultipartFile file);
    AttachmentResponse getCoverPhoto(String activityId);

    void deleteCoverPhoto(String activityId);
}
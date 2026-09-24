package com.movem.backend.shared.attachment.services;

import com.movem.backend.shared.attachment.dtos.responses.AttachmentResponse;
import com.movem.backend.shared.attachment.entities.Attachment;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface AttachmentService {
    AttachmentResponse upload(MultipartFile file);
    List<AttachmentResponse> getMyAttachments();
    void delete(Long attachmentId);
    AttachmentResponse toResponse(Attachment attachment);
    ResponseEntity<Resource> view(Long attachmentId);
    ResponseEntity<Resource> download(Long attachmentId);
    AttachmentResponse uploadClubProfile(Integer clubId, MultipartFile file);
    AttachmentResponse uploadClubCover(Integer clubId, MultipartFile file);
    List<AttachmentResponse> getClubAttachments(Integer clubId);
    AttachmentResponse uploadChallengeAttachment(Integer challengeId, MultipartFile file);
    List<AttachmentResponse> getChallengeAttachments(Integer challengeId);
}
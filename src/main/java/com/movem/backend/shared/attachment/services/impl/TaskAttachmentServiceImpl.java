package com.movem.backend.shared.attachment.services.impl;

import com.movem.backend.shared.attachment.dtos.responses.AttachmentResponse;
import com.movem.backend.shared.attachment.entities.Attachment;
import com.movem.backend.authentication.entities.User;
import com.movem.backend.task.entities.Task;
import com.movem.backend.commons.Exception.ResourceNotFoundException;
import com.movem.backend.shared.attachment.repositories.AttachmentRepository;
import com.movem.backend.task.repositories.TaskRepository;
import com.movem.backend.shared.attachment.services.AttachmentService;
import com.movem.backend.shared.attachment.services.TaskAttachmentService;
import com.movem.backend.authentication.services.CurrentUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TaskAttachmentServiceImpl implements TaskAttachmentService {

    private final TaskRepository taskRepository;
    private final AttachmentRepository attachmentRepository;
    private final AttachmentService attachmentService;
    private final CurrentUserService currentUserService;

    @Override
    public AttachmentResponse upload(String activityId, MultipartFile file) {
        User currentUser = currentUserService.getCurrentUser();
        Task task = taskRepository.findById(activityId).orElseThrow(() -> new ResourceNotFoundException("Task not found."));

        if (!task.getActivity().getUser().getId().equals(currentUser.getId())) {

            throw new IllegalArgumentException("You can only attach files to your own task.");
        }

        AttachmentResponse uploaded = attachmentService.upload(file);

        Attachment attachment = attachmentRepository.findById(uploaded.getId()).orElseThrow(() -> new ResourceNotFoundException("Uploaded attachment not found."));

        attachment.setTask(task);
        Attachment saved = attachmentRepository.save(attachment);

        return attachmentService.toResponse(saved);
    }

    @Override
    public List<AttachmentResponse> getAttachments(String activityId) {
        User currentUser = currentUserService.getCurrentUser();
        Task task = taskRepository.findById(activityId).orElseThrow(() -> new ResourceNotFoundException("Task not found."));

        if (!task.getActivity().getUser().getId().equals(currentUser.getId())) {

            throw new IllegalArgumentException("You can only view attachments from your own task.");
        }

        return attachmentRepository
                .findByTaskAndDeletedAtIsNull(task)
                .stream()
                .map(attachmentService::toResponse)
                .toList();
    }
}
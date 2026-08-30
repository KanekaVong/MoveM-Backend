package com.movem.backend.Service.Implement.Attachment;

import com.movem.backend.Dto.response.Attachment.AttachmentResponse;
import com.movem.backend.Entity.Attachment.Attachment;
import com.movem.backend.Entity.Auth.User;
import com.movem.backend.Entity.Tasks.Task;
import com.movem.backend.Exception.ResourceNotFoundException;
import com.movem.backend.Repository.AttachmentRepository.AttachmentRepository;
import com.movem.backend.Repository.TaskRepositories.TaskRepository;
import com.movem.backend.Service.AttachmentService.AttachmentService;
import com.movem.backend.Service.AttachmentService.TaskAttachmentService;
import com.movem.backend.Service.AuthServices.CurrentUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TaskAttachmentServiceImpl
        implements TaskAttachmentService {

    private final TaskRepository taskRepository;
    private final AttachmentRepository attachmentRepository;
    private final AttachmentService attachmentService;
    private final CurrentUserService currentUserService;

    @Override
    public AttachmentResponse upload(
            String activityId,
            MultipartFile file
    ) {

        User currentUser =
                currentUserService.getCurrentUser();

        Task task =
                taskRepository
                        .findById(activityId)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Task not found."
                                )
                        );

        // Make sure the user owns/has access to this task.
        if (!task.getActivity()
                .getUser()
                .getId()
                .equals(currentUser.getId())) {

            throw new IllegalArgumentException(
                    "You can only attach files to your own task."
            );
        }

        /*
         * First use the existing AttachmentService
         * to physically store the file.
         */
        AttachmentResponse uploaded =
                attachmentService.upload(file);

        Attachment attachment =
                attachmentRepository
                        .findById(uploaded.getId())
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Uploaded attachment not found."
                                )
                        );

        attachment.setTask(task);

        attachmentRepository.save(attachment);

        return uploaded;
    }

    @Override
    public List<AttachmentResponse> getAttachments(
            String activityId
    ) {

        User currentUser =
                currentUserService.getCurrentUser();

        Task task =
                taskRepository
                        .findById(activityId)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Task not found."
                                )
                        );

        if (!task.getActivity()
                .getUser()
                .getId()
                .equals(currentUser.getId())) {

            throw new IllegalArgumentException(
                    "You can only view attachments from your own task."
            );
        }

        return attachmentRepository
                .findByTaskAndDeletedAtIsNull(task)
                .stream()
                .map(this::toResponse)
                .toList();
    }

    private AttachmentResponse toResponse(
            Attachment attachment
    ) {

        return AttachmentResponse.builder()
                .id(attachment.getId())
                .originalFileName(
                        attachment.getOriginalFileName()
                )
                .fileType(
                        attachment.getFileType()
                )
                .fileSize(
                        attachment.getFileSize()
                )
                .filePath(
                        attachment.getFilePath()
                )
                .uploadedBy(
                        attachment.getUploadedBy().getId()
                )
                .createdAt(
                        attachment.getCreatedAt()
                )
                .build();
    }
}
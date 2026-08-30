package com.movem.backend.Dto.response.Attachment;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class AttachmentResponse {

    private Long id;

    private String originalFileName;

    private String fileType;

    private Long fileSize;

    private String filePath;

    private Integer uploadedBy;

    private LocalDateTime createdAt;
}
package com.movem.backend.shared.attachment.dtos.responses;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;

@Getter
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class AttachmentResponse {
     Long id;
     String originalFileName;
     String fileType;
     Long fileSize;
     String filePath;
     Integer uploadedBy;
     LocalDateTime createdAt;
     String url;
}
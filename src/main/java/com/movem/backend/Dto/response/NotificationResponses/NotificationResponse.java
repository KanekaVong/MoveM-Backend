package com.movem.backend.Dto.response.NotificationResponses;

import com.movem.backend.model.enums.Notification.NotificationType;
import com.movem.backend.model.enums.Notification.ReferenceType;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class NotificationResponse {

    private Long id;

    private Integer senderId;

    private String senderName;

    private String senderProfilePicture;

    private String title;

    private String message;

    private NotificationType notificationType;

    private ReferenceType referenceType;

    private String referenceId;

    private Boolean isRead;

    private LocalDateTime createdAt;

    private LocalDateTime readAt;

}
package com.movem.backend.shared.notification.dtos.responses;

import com.movem.backend.commons.enums.Notification.NotificationType;
import com.movem.backend.commons.enums.Notification.ReferenceType;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class NotificationResponse {
     Long id;
     Integer senderId;
     String senderName;
     String senderProfilePicture;
     String title;
     String message;
     NotificationType notificationType;
     ReferenceType referenceType;
     String referenceId;
     Boolean isRead;
     LocalDateTime createdAt;
     LocalDateTime readAt;

}
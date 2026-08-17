package com.movem.backend.Mapper.NotificationMapper;

import com.movem.backend.Dto.response.NotificationResponses.NotificationResponse;
import com.movem.backend.Entity.Shared.Notification;
import org.springframework.stereotype.Component;

import java.util.Base64;

@Component
public class NotificationMapper {

    public NotificationResponse toResponse(
            Notification notification
    ) {

        return NotificationResponse.builder()
                .id(notification.getId())

                .senderId(
                        notification.getSender() != null
                                ? notification.getSender().getId()
                                : null
                )

                .senderName(
                        notification.getSender() != null
                                ? notification.getSender().getUsername()
                                : null
                )

                .senderProfilePicture(
                        notification.getSender() != null &&
                                notification.getSender().getProfilePic() != null
                                ? Base64.getEncoder()
                                .encodeToString(notification.getSender().getProfilePic())
                                : null
                )

                .title(notification.getTitle())

                .message(notification.getMessage())

                .notificationType(notification.getNotificationType())

                .referenceType(notification.getReferenceType())

                .referenceId(notification.getReferenceId())

                .isRead(notification.getIsRead())

                .createdAt(notification.getCreatedAt())

                .readAt(notification.getReadAt())

                .build();

    }

}
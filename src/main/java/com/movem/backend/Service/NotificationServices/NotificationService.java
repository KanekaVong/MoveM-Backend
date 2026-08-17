package com.movem.backend.Service.NotificationServices;

import com.movem.backend.Dto.response.NotificationResponses.NotificationResponse;
import com.movem.backend.Entity.Auth.User;
import com.movem.backend.model.enums.Notification.NotificationType;
import com.movem.backend.model.enums.Notification.ReferenceType;

import java.util.List;

public interface NotificationService {

    List<NotificationResponse> getNotifications();

    List<NotificationResponse> getUnreadNotifications();

    Long getUnreadCount();

    void markAsRead(Long notificationId);

    void markAllAsRead();

    void deleteNotification(Long notificationId);

    void createNotification(
            User receiver,
            User sender,
            NotificationType notificationType,
            ReferenceType referenceType,
            String referenceId,
            String title,
            String message
    );
}
package com.movem.backend.shared.notification.services;

import com.movem.backend.shared.notification.dtos.responses.NotificationResponse;
import com.movem.backend.shared.activity.entities.Activity;
import com.movem.backend.authentication.entities.User;
import com.movem.backend.commons.enums.Notification.NotificationType;
import com.movem.backend.commons.enums.Notification.ReferenceType;

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

    void notifyActivityGroup(
            Activity activity,
            User sender,
            NotificationType notificationType,
            ReferenceType referenceType,
            String referenceId,
            String title,
            String message
    );

    List<NotificationResponse> getNotificationsByActivity(
            String activityId
    );
}
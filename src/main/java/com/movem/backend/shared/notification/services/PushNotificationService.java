package com.movem.backend.shared.notification.services;

import com.movem.backend.authentication.entities.User;

public interface PushNotificationService {
    void sendPushNotification(User receiver, String title, String message);
}

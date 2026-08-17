package com.movem.backend.Service.NotificationServices;

import com.movem.backend.Entity.Auth.User;

public interface PushNotificationService {

    void sendPushNotification(
            User receiver,
            String title,
            String message
    );
}

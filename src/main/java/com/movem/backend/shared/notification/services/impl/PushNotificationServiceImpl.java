package com.movem.backend.shared.notification.services.impl;

import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingException;
import com.google.firebase.messaging.Message;
import com.google.firebase.messaging.Notification;
import com.movem.backend.authentication.entities.User;
import com.movem.backend.authentication.entities.UserDevice;
import com.movem.backend.authentication.repositories.UserDeviceRepository;
import com.movem.backend.shared.notification.services.PushNotificationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class PushNotificationServiceImpl implements PushNotificationService {

    private final UserDeviceRepository userDeviceRepository;

    @Override
    public void sendPushNotification(User receiver, String title, String message) {

        List<UserDevice> devices = userDeviceRepository.findByUserAndIsActiveTrue(receiver);

        if (devices.isEmpty()) {
            log.info("No active notification devices found for user {}.", receiver.getId());
            return;
        }

        for (UserDevice device : devices) {
            String deviceToken = device.getDeviceToken();
            if (deviceToken == null || deviceToken.isBlank()) {
                continue;
            }

            Message firebaseMessage = Message.builder().setToken(deviceToken).setNotification(Notification.builder().setTitle(title).setBody(message).build()).build();

            try {
                String response = FirebaseMessaging.getInstance().send(firebaseMessage);
                log.info("FCM notification sent. user={}, device={}, response={}", receiver.getId(), device.getId(), response);
            } catch (FirebaseMessagingException e) {

                log.error("Failed to send FCM notification. user={}, device={}", receiver.getId(), device.getId(), e);
            }
        }
    }
}
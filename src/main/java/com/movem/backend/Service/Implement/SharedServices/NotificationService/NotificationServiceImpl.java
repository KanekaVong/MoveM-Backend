package com.movem.backend.Service.Implement.SharedServices.NotificationService;

import com.movem.backend.Dto.response.NotificationResponses.NotificationResponse;
import com.movem.backend.Entity.Activity.Activity;
import com.movem.backend.Entity.Collaboration.ActivityGroup;
import com.movem.backend.Entity.Shared.GroupMember;
import com.movem.backend.Entity.Shared.Notification;
import com.movem.backend.Entity.Auth.User;
import com.movem.backend.Exception.ResourceNotFoundException;
import com.movem.backend.Mapper.NotificationMapper.NotificationMapper;
import com.movem.backend.Repository.CollaborationRepository.GroupRepository;
import com.movem.backend.Repository.NotificationRepository.NotificationRepository;
import com.movem.backend.Repository.SharedRepository.GroupMemberRepository;
import com.movem.backend.Service.AuthServices.CurrentUserService;
import com.movem.backend.Service.NotificationServices.NotificationService;
import com.movem.backend.Service.NotificationServices.PushNotificationService;
import com.movem.backend.model.enums.Notification.NotificationType;
import com.movem.backend.model.enums.Notification.ReferenceType;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class NotificationServiceImpl implements NotificationService {

    private final NotificationRepository notificationRepository;
    private final NotificationMapper notificationMapper;
    private final CurrentUserService currentUserService;
    private final GroupRepository groupRepository;
    private final GroupMemberRepository groupMemberRepository;
    private final PushNotificationService pushNotificationService;

    @Override
    public List<NotificationResponse> getNotifications() {

        User currentUser =
                currentUserService.getCurrentUser();

        return notificationRepository
                .findByUserOrderByCreatedAtDesc(currentUser)
                .stream()
                .map(notificationMapper::toResponse)
                .toList();
    }


    @Override
    public List<NotificationResponse> getUnreadNotifications() {

        User currentUser =
                currentUserService.getCurrentUser();

        return notificationRepository
                .findByUserAndIsReadFalseOrderByCreatedAtDesc(
                        currentUser
                )
                .stream()
                .map(notificationMapper::toResponse)
                .toList();
    }

    @Override
    public List<NotificationResponse> getNotificationsByActivity(
            String activityId
    ) {

        User currentUser =
                currentUserService.getCurrentUser();

        return notificationRepository
                .findByUserAndReferenceIdOrderByCreatedAtDesc(
                        currentUser,
                        activityId
                )
                .stream()
                .map(notificationMapper::toResponse)
                .toList();
    }

    @Override
    public Long getUnreadCount() {

        User currentUser =
                currentUserService.getCurrentUser();

        return notificationRepository
                .countByUserAndIsReadFalse(currentUser);
    }


    @Override
    public void markAsRead(Long notificationId) {

        User currentUser =
                currentUserService.getCurrentUser();

        Notification notification =
                notificationRepository
                        .findByIdAndUser(
                                notificationId,
                                currentUser
                        )
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Notification not found."
                                )
                        );

        if (Boolean.TRUE.equals(notification.getIsRead())) {
            return;
        }

        notification.setIsRead(true);
        notification.setReadAt(LocalDateTime.now());

        notificationRepository.save(notification);
    }


    @Override
    public void markAllAsRead() {

        User currentUser =
                currentUserService.getCurrentUser();

        List<Notification> notifications =
                notificationRepository
                        .findByUserAndIsReadFalseOrderByCreatedAtDesc(
                                currentUser
                        );

        LocalDateTime now = LocalDateTime.now();

        for (Notification notification : notifications) {

            notification.setIsRead(true);
            notification.setReadAt(now);
        }

        notificationRepository.saveAll(notifications);
    }


    @Override
    public void deleteNotification(Long notificationId) {

        User currentUser =
                currentUserService.getCurrentUser();

        Notification notification =
                notificationRepository
                        .findByIdAndUser(
                                notificationId,
                                currentUser
                        )
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Notification not found."
                                )
                        );

        notificationRepository.delete(notification);
    }


    @Override
    public void createNotification(
            User receiver,
            User sender,
            NotificationType notificationType,
            ReferenceType referenceType,
            String referenceId,
            String title,
            String message
    ) {

        Notification notification =
                new Notification();

        notification.setUser(receiver);
        notification.setSender(sender);

        notification.setNotificationType(
                notificationType
        );

        notification.setReferenceType(
                referenceType
        );

        notification.setReferenceId(
                referenceId
        );

        notification.setTitle(title);
        notification.setMessage(message);

        notification.setIsRead(false);
        notification.setCreatedAt(
                LocalDateTime.now()
        );

        notificationRepository.save(notification);

        pushNotificationService.sendPushNotification(
                receiver,
                title,
                message
        );
    }

    @Override
    public void notifyActivityGroup(
            Activity activity,
            User sender,
            NotificationType notificationType,
            ReferenceType referenceType,
            String referenceId,
            String title,
            String message
    ) {

        ActivityGroup group =
                groupRepository
                        .findByActivity(activity)
                        .orElse(null);

        if (group == null) {
            return;
        }

        List<GroupMember> members =
                groupMemberRepository.findByActivityGroup(group);

        for (GroupMember member : members) {

            User receiver = member.getUser();

            if (receiver.getId().equals(sender.getId())) {
                continue;
            }

            createNotification(
                    receiver,
                    sender,
                    notificationType,
                    referenceType,
                    referenceId,
                    title,
                    message
            );
        }
    }
}
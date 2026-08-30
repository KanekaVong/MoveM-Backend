package com.movem.backend.Service.Event;

import com.movem.backend.Entity.Auth.User;
import com.movem.backend.Event.FeatureEvent;
import com.movem.backend.Service.FitnessServices.Achievement.AchievementService;
import com.movem.backend.Service.NotificationServices.NotificationService;
import com.movem.backend.Service.SharedServices.ActivityFeedService;
import com.movem.backend.Service.SharedServices.AuditLogService;
import com.movem.backend.model.enums.Activity.ActivityFeedEvent;
import com.movem.backend.model.enums.Audit.AuditCategory;
import com.movem.backend.model.enums.Audit.AuditSeverity;
import com.movem.backend.model.enums.Event.FeatureEventAction;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class FeatureEventTrackingServiceImpl
        implements FeatureEventTrackingService {

    private final AchievementService achievementService;
    private final ActivityFeedService activityFeedService;
    private final AuditLogService auditLogService;
    private final NotificationService notificationService;

    @Override
    public void handle(FeatureEvent event) {

        handleActivityFeed(event);
        handleAuditLog(event);
        handleNotification(event);

        achievementService.evaluate(
                event.getActor(),
                event
        );
    }

    private void handleActivityFeed(FeatureEvent event) {

        if (!event.getActions().contains(
                FeatureEventAction.ACTIVITY_FEED
        )) {
            return;
        }

        String referenceId = event.getFeedReferenceId();

        activityFeedService.createFeed(
                event.getActivity(),
                event.getActor(),
                event.getFeedEvent(),
                event.getFeedMessage(),
                referenceId
        );
    }

    private void handleAuditLog(FeatureEvent event) {

        if (!event.getActions().contains(
                FeatureEventAction.AUDIT_LOG
        )) {
            return;
        }

        auditLogService.createLog(
                event.getActivity(),
                event.getActor(),
                event.getFeedEvent(),
                event.getAuditCategory(),
                event.getAuditSeverity(),
                event.getAuditEntity(),
                event.getAuditMessage(),
                event.getOldValue(),
                event.getNewValue()
        );
    }

    private void handleNotification(
            FeatureEvent event
    ) {

        if (!event.getActions().contains(
                FeatureEventAction.NOTIFICATION
        )) {
            return;
        }

        if (event.isNotifyActivityGroup()) {

            if (event.getActivity() == null
                    || event.getNotificationType() == null) {
                return;
            }

            notificationService.notifyActivityGroup(
                    event.getActivity(),
                    event.getActor(),
                    event.getNotificationType(),
                    event.getReferenceType(),
                    event.getReferenceId(),
                    event.getNotificationTitle(),
                    event.getNotificationMessage()
            );

            return;
        }

        if (event.getNotificationReceiver() == null
                || event.getNotificationType() == null) {
            return;
        }

        notificationService.createNotification(
                event.getNotificationReceiver(),
                event.getActor(),
                event.getNotificationType(),
                event.getReferenceType(),
                event.getReferenceId(),
                event.getNotificationTitle(),
                event.getNotificationMessage()
        );
    }

    @Override
    public void handleDeletedActivity(
            String activityId,
            String activityName,
            User actor
    ) {

        auditLogService.createDeletedActivityLog(
                activityId,
                activityName,
                actor,
                ActivityFeedEvent.ACTIVITY_HARD_DELETED,
                AuditCategory.TASK,
                AuditSeverity.WARNING,
                "activity",
                "Activity permanently deleted.",
                activityName,
                null
        );
    }
}
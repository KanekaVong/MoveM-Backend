package com.movem.backend.Service.Implement.SharedServices.Event;

import com.movem.backend.Event.FeatureEvent;
import com.movem.backend.Service.NotificationServices.NotificationService;
import com.movem.backend.Service.SharedServices.ActivityFeedService;
import com.movem.backend.Service.SharedServices.AuditLogService;
import com.movem.backend.Service.SharedServices.Event.FeatureEventTrackingService;
import com.movem.backend.model.enums.Event.FeatureEventAction;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class FeatureEventTrackingServiceImpl
        implements FeatureEventTrackingService {

    private final ActivityFeedService activityFeedService;
    private final AuditLogService auditLogService;
    private final NotificationService notificationService;

    @Override
    public void handle(FeatureEvent event) {

        if (event.getActions().contains(
                FeatureEventAction.ACTIVITY_FEED
        )) {

            activityFeedService.createFeed(
                    event.getActivity(),
                    event.getActor(),
                    event.getFeedEvent(),
                    event.getAuditMessage(),
                    Long.valueOf(event.getReferenceId())
            );
        }

        if (event.getActions().contains(
                FeatureEventAction.AUDIT_LOG
        )) {

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

        if (event.getActions().contains(
                FeatureEventAction.NOTIFICATION
        )) {

            if (
                    event.getNotificationReceiver() != null &&
                            event.getNotificationType() != null
            ) {

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
        }
    }
}
package com.movem.backend.Service.Event.Factory.Trip;

import com.movem.backend.Entity.Auth.User;
import com.movem.backend.Entity.Trip.Trip;
import com.movem.backend.Entity.Trip.TripExpense;
import com.movem.backend.Entity.Trip.TripStop;
import com.movem.backend.Event.FeatureEvent;
import com.movem.backend.model.enums.Activity.ActivityFeedEvent;
import com.movem.backend.model.enums.Audit.AuditCategory;
import com.movem.backend.model.enums.Audit.AuditSeverity;
import com.movem.backend.model.enums.Event.FeatureEventAction;
import com.movem.backend.model.enums.Notification.NotificationType;
import com.movem.backend.model.enums.Notification.ReferenceType;
import org.springframework.stereotype.Component;

import java.util.Set;

@Component
public class TripEventFactory {

    public FeatureEvent created(
            Trip trip,
            User actor
    ) {

        return FeatureEvent.builder()
                .activity(trip.getActivity())
                .actor(actor)
                .feedEvent(ActivityFeedEvent.TRIP_CREATED)
                .feedMessage(
                        actor.getUsername()
                                + " planned a trip: "
                                + trip.getActivity().getActivityName()
                )
                .actions(Set.of(
                        FeatureEventAction.AUDIT_LOG,
                        FeatureEventAction.ACTIVITY_FEED
                ))
                .auditCategory(AuditCategory.TRIP)
                .auditSeverity(AuditSeverity.INFO)
                .auditEntity("trip")
                .auditMessage(
                        actor.getUsername()
                                + " created the trip: "
                                + trip.getActivity().getActivityName()
                )
                .newValue(
                        trip.getActivity().getActivityName()
                )
                .build();
    }

    public FeatureEvent updated(
            Trip trip,
            User actor
    ) {

        return FeatureEvent.builder()
                .activity(trip.getActivity())
                .actor(actor)
                .feedEvent(ActivityFeedEvent.TRIP_UPDATED)
                .feedMessage(
                        actor.getUsername()
                                + " updated the trip: "
                                + trip.getActivity().getActivityName()
                )
                .actions(Set.of(
                        FeatureEventAction.AUDIT_LOG,
                        FeatureEventAction.ACTIVITY_FEED,
                        FeatureEventAction.NOTIFICATION
                ))
                .auditCategory(AuditCategory.TRIP)
                .auditSeverity(AuditSeverity.INFO)
                .auditEntity("trip")
                .auditMessage(
                        actor.getUsername()
                                + " updated the trip: "
                                + trip.getActivity().getActivityName()
                )
                .newValue(
                        trip.getActivity().getActivityName()
                )
                .notifyActivityGroup(true)
                .notificationType(
                        NotificationType.TRIP_UPDATED
                )
                .referenceType(
                        ReferenceType.TRIP
                )
                .referenceId(
                        trip.getActivity().getId()
                )
                .notificationTitle(
                        "Trip Updated"
                )
                .notificationMessage(
                        actor.getUsername()
                                + " updated "
                                + trip.getActivity().getActivityName()
                )
                .build();
    }

    public FeatureEvent deleted(
            Trip trip,
            User actor
    ) {

        return FeatureEvent.builder()
                .activity(trip.getActivity())
                .actor(actor)
                .feedEvent(ActivityFeedEvent.TRIP_DELETED)
                .feedMessage(
                        actor.getUsername()
                                + " deleted the trip: "
                                + trip.getActivity().getActivityName()
                )
                .actions(Set.of(
                        FeatureEventAction.AUDIT_LOG,
                        FeatureEventAction.ACTIVITY_FEED,
                        FeatureEventAction.NOTIFICATION
                ))
                .auditCategory(AuditCategory.TRIP)
                .auditSeverity(AuditSeverity.WARNING)
                .auditEntity("trip")
                .auditMessage(
                        actor.getUsername()
                                + " deleted the trip: "
                                + trip.getActivity().getActivityName()
                )
                .oldValue(
                        trip.getActivity().getActivityName()
                )
                .newValue(
                        trip.getActivity().getActivityName()
                )
                .notifyActivityGroup(true)
                .notificationType(
                        NotificationType.TRIP_UPDATED
                )
                .referenceType(
                        ReferenceType.TRIP
                )
                .referenceId(
                        trip.getActivity().getId()
                )
                .notificationTitle(
                        "Trip Deleted"
                )
                .notificationMessage(
                        actor.getUsername()
                                + " deleted the trip: "
                                + trip.getActivity().getActivityName()
                )
                .build();
    }

    public FeatureEvent restored(
            Trip trip,
            User actor
    ) {

        return FeatureEvent.builder()
                .activity(trip.getActivity())
                .actor(actor)
                .feedEvent(ActivityFeedEvent.TRIP_RESTORED)
                .feedMessage(
                        actor.getUsername()
                                + " restored the trip: "
                                + trip.getActivity().getActivityName()
                )
                .actions(Set.of(
                        FeatureEventAction.AUDIT_LOG,
                        FeatureEventAction.ACTIVITY_FEED,
                        FeatureEventAction.NOTIFICATION
                ))
                .auditCategory(AuditCategory.TRIP)
                .auditSeverity(AuditSeverity.INFO)
                .auditEntity("trip")
                .auditMessage(
                        actor.getUsername()
                                + " restored the trip: "
                                + trip.getActivity().getActivityName()
                )
                .newValue(
                        trip.getActivity().getActivityName()
                )
                .notifyActivityGroup(true)
                .notificationType(
                        NotificationType.TRIP_UPDATED
                )
                .referenceType(
                        ReferenceType.TRIP
                )
                .referenceId(
                        trip.getActivity().getId()
                )
                .notificationTitle(
                        "Trip Restored"
                )
                .notificationMessage(
                        actor.getUsername()
                                + " restored the trip: "
                                + trip.getActivity().getActivityName()
                )
                .build();
    }

    public FeatureEvent stopAdded(
            TripStop stop,
            User actor
    ) {

        return FeatureEvent.builder()
                .activity(
                        stop.getTrip().getActivity()
                )
                .actor(actor)
                .feedEvent(
                        ActivityFeedEvent.STOP_ADDED
                )
                .feedMessage(
                        actor.getUsername()
                                + " added a stop: "
                                + stop.getLocationName()
                )
                .referenceId(
                        stop.getTrip()
                                .getActivity()
                                .getId()
                )
                .actions(Set.of(
                        FeatureEventAction.AUDIT_LOG,
                        FeatureEventAction.ACTIVITY_FEED,
                        FeatureEventAction.NOTIFICATION
                ))
                .auditCategory(AuditCategory.TRIP)
                .auditSeverity(AuditSeverity.INFO)
                .auditEntity("trip_stop")
                .auditMessage(
                        actor.getUsername()
                                + " added a stop: "
                                + stop.getLocationName()
                )
                .newValue(
                        stop.getLocationName()
                )
                .notifyActivityGroup(true)
                .notificationType(
                        NotificationType.TRIP_UPDATED
                )
                .referenceType(
                        ReferenceType.TRIP
                )
                .notificationTitle(
                        "Trip Stop Added"
                )
                .notificationMessage(
                        actor.getUsername()
                                + " added a stop: "
                                + stop.getLocationName()
                )
                .build();
    }

    public FeatureEvent stopCompleted(
            TripStop stop,
            User actor
    ) {

        return FeatureEvent.builder()
                .activity(
                        stop.getTrip().getActivity()
                )
                .actor(actor)
                .feedEvent(
                        ActivityFeedEvent.STOP_COMPLETED
                )
                .feedMessage(
                        actor.getUsername()
                                + " checked off "
                                + stop.getLocationName()
                )
                .referenceId(
                        stop.getTrip()
                                .getActivity()
                                .getId()
                )
                .actions(Set.of(
                        FeatureEventAction.AUDIT_LOG,
                        FeatureEventAction.ACTIVITY_FEED,
                        FeatureEventAction.NOTIFICATION
                ))
                .auditCategory(AuditCategory.TRIP)
                .auditSeverity(AuditSeverity.INFO)
                .auditEntity("trip_stop")
                .auditMessage(
                        actor.getUsername()
                                + " completed the stop: "
                                + stop.getLocationName()
                )
                .newValue(
                        stop.getLocationName()
                )
                .notifyActivityGroup(true)
                .notificationType(
                        NotificationType.TRIP_UPDATED
                )
                .referenceType(
                        ReferenceType.TRIP
                )
                .notificationTitle(
                        "Trip Stop Completed"
                )
                .notificationMessage(
                        actor.getUsername()
                                + " completed the stop: "
                                + stop.getLocationName()
                )
                .build();
    }

    public FeatureEvent expenseLogged(
            TripExpense expense,
            User actor
    ) {

        Trip trip =
                expense.getBudget().getTrip();

        return FeatureEvent.builder()
                .activity(trip.getActivity())
                .actor(actor)
                .feedEvent(ActivityFeedEvent.EXPENSE_LOGGED)
                .feedMessage(
                        actor.getUsername()
                                + " logged "
                                + expense.getAmount()
                                + " under "
                                + expense.getBudget().getCategory()
                )
                .referenceId(
                        expense.getBudget()
                                .getTrip()
                                .getActivity()
                                .getId()
                )
                .actions(Set.of(
                        FeatureEventAction.AUDIT_LOG,
                        FeatureEventAction.ACTIVITY_FEED,
                        FeatureEventAction.NOTIFICATION
                ))
                .auditCategory(AuditCategory.TRIP)
                .auditSeverity(AuditSeverity.INFO)
                .auditEntity("trip_expense")
                .auditMessage(
                        actor.getUsername()
                                + " logged "
                                + expense.getAmount()
                                + " under "
                                + expense.getBudget().getCategory()
                )
                .newValue(
                        expense.getAmount().toString()
                )
                .notifyActivityGroup(true)
                .notificationType(
                        NotificationType.TRIP_EXPENSE_LOGGED
                )
                .referenceType(
                        ReferenceType.TRIP
                )
                .notificationTitle(
                        "New Trip Expense"
                )
                .notificationMessage(
                        actor.getUsername()
                                + " logged "
                                + expense.getAmount()
                                + " under "
                                + expense.getBudget().getCategory()
                )
                .build();
    }

}
package com.movem.backend.shared.historyandlogs.featureevents.services;

import com.movem.backend.authentication.entities.User;
import com.movem.backend.commons.Event.FeatureEvent;

public interface FeatureEventTrackingService {

    void handle(FeatureEvent event);

    void handleDeletedActivity(
            String activityId,
            String activityName,
            User actor
    );
}
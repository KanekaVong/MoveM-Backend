package com.movem.backend.Service.Event;

import com.movem.backend.Entity.Auth.User;
import com.movem.backend.Event.FeatureEvent;

public interface FeatureEventTrackingService {

    void handle(FeatureEvent event);

    void handleDeletedActivity(
            String activityId,
            String activityName,
            User actor
    );
}
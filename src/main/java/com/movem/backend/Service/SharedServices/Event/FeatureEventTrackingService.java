package com.movem.backend.Service.SharedServices.Event;

import com.movem.backend.Event.FeatureEvent;

public interface FeatureEventTrackingService {

    void handle(FeatureEvent event);

}
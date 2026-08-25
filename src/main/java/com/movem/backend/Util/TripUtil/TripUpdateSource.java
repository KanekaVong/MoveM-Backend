package com.movem.backend.Util.TripUtil;

import com.movem.backend.Util.Base.BaseActivityUpdateSource;
import com.movem.backend.model.enums.Activity.ActivityStatus;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public interface TripUpdateSource
        extends BaseActivityUpdateSource {
    String getActivityName();

    String getDescription();

    LocalDateTime getStartActivity();

    LocalDateTime getDeadline();

    String getLocationName();

    String getLocationAddress();

    BigDecimal getLat();

    BigDecimal getLng();

    String getGooglePlaceId();

    String getCoordinates();

    ActivityStatus getStatus();
}

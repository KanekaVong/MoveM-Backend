package com.movem.backend.commons.Util.TripUtil;

import com.movem.backend.commons.Util.BaseUtil.BaseActivityUpdateSource;
import com.movem.backend.commons.enums.shared.ActivityStatus;

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

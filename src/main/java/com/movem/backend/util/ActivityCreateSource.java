package com.movem.backend.util;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public interface ActivityCreateSource {

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

    String getParentActivityId();


}
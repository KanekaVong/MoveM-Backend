package com.movem.backend.commons.Util.TripUtil;

import com.movem.backend.commons.Util.BaseUtil.BaseActivityCreateSource;

import java.math.BigDecimal;

public interface TripCreateSource extends BaseActivityCreateSource {

    String getLocationName();

    String getLocationAddress();

    BigDecimal getLat();

    BigDecimal getLng();

    String getGooglePlaceId();

    String getCoordinates();

    String getParentActivityId();


}
package com.movem.backend.commons.Util.BaseUtil;

import com.movem.backend.commons.enums.shared.ActivityStatus;

import java.time.LocalDateTime;

public interface BaseActivityUpdateSource {

    String getActivityName();

    String getDescription();

    LocalDateTime getDeadline();

    ActivityStatus getStatus();
}
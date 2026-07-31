package com.movem.backend.util.Base;

import com.movem.backend.model.enums.Activity.ActivityStatus;

import java.time.LocalDateTime;

public interface BaseActivityUpdateSource {

    String getActivityName();

    String getDescription();

    LocalDateTime getDeadline();

    ActivityStatus getStatus();
}
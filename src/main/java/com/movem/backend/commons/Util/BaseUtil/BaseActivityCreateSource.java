package com.movem.backend.commons.Util.BaseUtil;

import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDateTime;

public interface BaseActivityCreateSource {

    String getActivityName();

    String getDescription();

    LocalDateTime getStartActivity();

    LocalDateTime getDeadline();

    @Schema(hidden = true)
    String getParentActivityId();

}
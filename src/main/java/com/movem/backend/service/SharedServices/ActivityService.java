package com.movem.backend.service.SharedServices;

import com.movem.backend.entity.Activity.Activity;
import com.movem.backend.entity.User;
import com.movem.backend.model.enums.Activity.ActivityType;
import com.movem.backend.util.Base.BaseActivityCreateSource;
import com.movem.backend.util.Base.BaseActivityUpdateSource;

import java.util.List;

public interface ActivityService {

    Activity createActivity(
            BaseActivityCreateSource source,
            User user,
            ActivityType activityType
    );

    Activity attachLabels(
            Activity activity,
            List<Integer> labelIds
    );

    Activity updateActivity(
            Activity activity,
            BaseActivityUpdateSource source
    );

    void permanentlyDeleteActivity(String activityId);
}
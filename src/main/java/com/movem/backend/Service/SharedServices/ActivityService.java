package com.movem.backend.Service.SharedServices;

import com.movem.backend.Entity.Activity.Activity;
import com.movem.backend.Entity.Auth.User;
import com.movem.backend.model.enums.Activity.ActivityType;
import com.movem.backend.Util.Base.BaseActivityCreateSource;
import com.movem.backend.Util.Base.BaseActivityUpdateSource;

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
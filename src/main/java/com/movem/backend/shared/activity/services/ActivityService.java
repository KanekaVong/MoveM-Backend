package com.movem.backend.shared.activity.services;

import com.movem.backend.shared.activity.entities.Activity;
import com.movem.backend.authentication.entities.User;
import com.movem.backend.commons.enums.shared.ActivityType;
import com.movem.backend.commons.Util.BaseUtil.BaseActivityCreateSource;
import com.movem.backend.commons.Util.BaseUtil.BaseActivityUpdateSource;

import java.util.List;

public interface ActivityService {

    Activity createActivity(BaseActivityCreateSource source, User user, ActivityType activityType);

    Activity attachLabels(Activity activity, List<Integer> labelIds);

    Activity updateActivity(Activity activity, BaseActivityUpdateSource source);

    void permanentlyDeleteActivity(String activityId);
}
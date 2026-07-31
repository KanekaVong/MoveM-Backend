package com.movem.backend.service.SharedServices;

import com.movem.backend.entity.Activity.Activity;
import com.movem.backend.entity.User;
import com.movem.backend.model.enums.Activity.ActivityType;
import com.movem.backend.util.ActivityCreateSource;
import com.movem.backend.util.ActivityUpdateSource;

import java.util.List;

public interface ActivityService {

    Activity createActivity(
            ActivityCreateSource source,
            User user,
            ActivityType activityType
    );

    Activity attachLabels(
            Activity activity,
            List<Integer> labelIds
    );

    Activity updateActivity(
            Activity activity,
            ActivityUpdateSource source
    );
}
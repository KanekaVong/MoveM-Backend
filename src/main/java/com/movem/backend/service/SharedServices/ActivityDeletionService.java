package com.movem.backend.service.SharedServices;

import com.movem.backend.entity.Activity.Activity;

public interface ActivityDeletionService {

    void permanentlyDelete(Activity activity);

}

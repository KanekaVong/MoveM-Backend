package com.movem.backend.Service.SharedServices;

import com.movem.backend.Entity.Activity.Activity;

public interface ActivityDeletionService {

    void permanentlyDelete(Activity activity);

}

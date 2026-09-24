package com.movem.backend.shared.activity.services;

import com.movem.backend.shared.activity.entities.Activity;

public interface ActivityDeletionService {

    void permanentlyDelete(Activity activity);

}

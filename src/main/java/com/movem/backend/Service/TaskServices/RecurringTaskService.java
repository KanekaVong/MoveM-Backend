package com.movem.backend.Service.TaskServices;

import com.movem.backend.Entity.Tasks.Task;

public interface RecurringTaskService {

    void generateNextOccurrence(Task completedTask);

}

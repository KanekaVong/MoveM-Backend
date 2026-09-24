package com.movem.backend.task.services;

import com.movem.backend.task.entities.Task;

public interface RecurringTaskService {

    void generateNextOccurrence(Task completedTask);

}

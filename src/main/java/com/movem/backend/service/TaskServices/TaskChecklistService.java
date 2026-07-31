package com.movem.backend.service.TaskServices;

import com.movem.backend.dto.request.TaskRequests.Create.CreateChecklistItemRequest;
import com.movem.backend.entity.Tasks.Task;

import java.util.List;

public interface TaskChecklistService {

    void createChecklistItems(
            Task task,
            List<CreateChecklistItemRequest> items
    );

    void markChecklistCompleted(Integer id);

}
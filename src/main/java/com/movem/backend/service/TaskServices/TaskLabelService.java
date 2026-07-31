package com.movem.backend.service.TaskServices;

import com.movem.backend.dto.request.TaskRequests.Create.CreateTaskLabelRequest;
import com.movem.backend.dto.request.TaskRequests.Update.UpdateTaskLabelRequest;
import com.movem.backend.dto.response.TaskResponses.TaskLabelResponse;

import java.util.List;

public interface TaskLabelService {

    TaskLabelResponse create(CreateTaskLabelRequest request);

    List<TaskLabelResponse> getMyLabels();

    TaskLabelResponse update(Integer id,
                             UpdateTaskLabelRequest request);

    void delete(Integer id);
}

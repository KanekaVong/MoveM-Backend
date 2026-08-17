package com.movem.backend.Service.TaskServices;

import com.movem.backend.Dto.request.TaskRequests.Create.CreateTaskLabelRequest;
import com.movem.backend.Dto.request.TaskRequests.Update.UpdateTaskLabelRequest;
import com.movem.backend.Dto.response.TaskResponses.TaskLabelResponse;

import java.util.List;

public interface TaskLabelService {

    TaskLabelResponse create(CreateTaskLabelRequest request);

    List<TaskLabelResponse> getMyLabels();

    TaskLabelResponse update(Integer id,
                             UpdateTaskLabelRequest request);

    void delete(Integer id);
}

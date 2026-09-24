package com.movem.backend.task.services;

import com.movem.backend.task.dtos.requests.Create.CreateTaskLabelRequest;
import com.movem.backend.task.dtos.requests.Update.UpdateTaskLabelRequest;
import com.movem.backend.task.dtos.responses.TaskLabelResponse;

import java.util.List;

public interface TaskLabelService {

    TaskLabelResponse create(CreateTaskLabelRequest request);

    List<TaskLabelResponse> getMyLabels();

    TaskLabelResponse update(Integer id,
                             UpdateTaskLabelRequest request);

    void delete(Integer id);
}

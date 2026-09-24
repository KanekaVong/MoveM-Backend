package com.movem.backend.task.mappers;

import com.movem.backend.task.dtos.responses.TaskResponse;
import com.movem.backend.task.entities.Task;
import com.movem.backend.commons.BaseMapper.BaseMapper;


public interface TaskMapper extends BaseMapper<Task, TaskResponse> {
}
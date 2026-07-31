package com.movem.backend.mapper.TaskMapper;

import com.movem.backend.dto.response.TaskResponses.TaskLabelResponse;
import com.movem.backend.entity.Tasks.TaskLabel;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Set;

@Component
public class TaskLabelMapper {

    public TaskLabelResponse toResponse(TaskLabel label) {

        if (label == null) {
            return null;
        }

        return TaskLabelResponse.builder()
                .id(label.getId())
                .name(label.getName())
                .color(label.getColor())
                .build();
    }

    public List<TaskLabelResponse> toResponseList(
            Set<TaskLabel> labels
    ) {

        return labels.stream()
                .map(this::toResponse)
                .toList();
    }
}

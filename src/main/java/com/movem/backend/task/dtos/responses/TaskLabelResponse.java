package com.movem.backend.task.dtos.responses;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class TaskLabelResponse {

    private Integer id;

    private String name;

    private String color;
}

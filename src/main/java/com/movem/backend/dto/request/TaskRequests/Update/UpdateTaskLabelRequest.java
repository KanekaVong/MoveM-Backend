package com.movem.backend.dto.request.TaskRequests.Update;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdateTaskLabelRequest {

    @NotBlank
    @Size(max = 50)
    private String name;

    @NotBlank
    @Size(max = 20)
    private String color;
}

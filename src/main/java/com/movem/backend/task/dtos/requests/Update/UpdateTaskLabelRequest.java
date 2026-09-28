package com.movem.backend.task.dtos.requests.Update;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UpdateTaskLabelRequest {
    @NotBlank
    @Size(max = 50)
    String name;

    @NotBlank
    @Size(max = 20)
    String color;
}

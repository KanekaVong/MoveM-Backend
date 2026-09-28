package com.movem.backend.task.dtos.requests.Create;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CreateTaskLabelRequest {
    @NotBlank(message = "Label name is required")
    @Size(max = 50)
    String name;
    @NotBlank
    @Pattern(regexp = "^#([A-Fa-f0-9]{6})$", message = "Colour must be a valid hex code.")
    String color;
}

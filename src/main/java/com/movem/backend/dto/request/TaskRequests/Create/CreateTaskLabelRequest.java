package com.movem.backend.dto.request.TaskRequests.Create;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateTaskLabelRequest {

    @NotBlank(message = "Label name is required")
    @Size(max = 50)
    private String name;

    @NotBlank
    @Pattern(
            regexp = "^#([A-Fa-f0-9]{6})$",
            message = "Colour must be a valid hex code."
    )
    private String color;
}

package com.movem.backend.task.dtos.requests.Create;

import jakarta.validation.constraints.NotBlank;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CreateChecklistItemRequest {
    @NotBlank(message = "Checklist item name is required.")
    String itemName;
}
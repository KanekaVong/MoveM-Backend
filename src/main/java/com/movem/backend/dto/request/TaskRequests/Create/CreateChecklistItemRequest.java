package com.movem.backend.dto.request.TaskRequests.Create;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateChecklistItemRequest {

    @NotBlank(message = "Checklist item name is required.")
    private String itemName;

}
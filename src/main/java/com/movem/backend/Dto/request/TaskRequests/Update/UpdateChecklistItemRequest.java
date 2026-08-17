package com.movem.backend.Dto.request.TaskRequests.Update;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdateChecklistItemRequest {

    @NotBlank
    private String itemName;

}

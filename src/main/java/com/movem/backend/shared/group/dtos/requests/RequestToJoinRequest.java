package com.movem.backend.shared.group.dtos.requests;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RequestToJoinRequest {

    @NotBlank
    private String joinToken;

}

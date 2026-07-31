package com.movem.backend.dto.request.GroupRequest;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RequestToJoinRequest {

    @NotBlank
    private String joinToken;

}

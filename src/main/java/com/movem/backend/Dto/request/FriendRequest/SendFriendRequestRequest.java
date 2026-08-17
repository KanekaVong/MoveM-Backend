package com.movem.backend.Dto.request.FriendRequest;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SendFriendRequestRequest {

    @NotBlank
    private String username;

}

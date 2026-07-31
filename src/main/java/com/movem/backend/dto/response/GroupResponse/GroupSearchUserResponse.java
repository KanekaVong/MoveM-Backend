package com.movem.backend.dto.response.GroupResponse;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GroupSearchUserResponse {

    private Integer userId;

    private String username;

    private String firstname;

    private String lastname;

    private String email;

}

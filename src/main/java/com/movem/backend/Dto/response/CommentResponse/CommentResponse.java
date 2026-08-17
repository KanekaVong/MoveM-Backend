package com.movem.backend.Dto.response.CommentResponse;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class CommentResponse {
    private Long id;

    private Integer userId;

    private String username;

    private String firstname;

    private String lastname;

    private String profilePic;

    private String content;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    private boolean edited;
}

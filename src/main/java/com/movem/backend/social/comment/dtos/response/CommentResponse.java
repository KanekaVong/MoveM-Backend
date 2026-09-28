package com.movem.backend.social.comment.dtos.response;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;

@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CommentResponse {
     Long id;
     Integer userId;
     String username;
     String firstname;
     String lastname;
     String profilePic;
     String content;
     LocalDateTime createdAt;
     LocalDateTime updatedAt;
     boolean edited;
}

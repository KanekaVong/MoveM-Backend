package com.movem.backend.Mapper.CommentMapper;


import com.movem.backend.Dto.response.CommentResponse.CommentResponse;
import com.movem.backend.Entity.Social.Comment;
import com.movem.backend.Mapper.BaseMapper.AbstractBaseMapper;
import org.springframework.stereotype.Component;

import java.util.Base64;

@Component
public class CommentMapper
        extends AbstractBaseMapper<Comment, CommentResponse> {

    @Override
    public CommentResponse toResponse(Comment comment) {

        CommentResponse response =
                new CommentResponse();

        response.setId(
                comment.getId()
        );

        response.setUserId(
                comment.getUser().getId()
        );

        response.setUsername(
                comment.getUser().getUsername()
        );

        response.setFirstname(
                comment.getUser().getFirstname()
        );

        response.setLastname(
                comment.getUser().getLastname()
        );

        if (comment.getUser().getProfilePic() != null) {

            response.setProfilePic(
                    Base64.getEncoder()
                            .encodeToString(
                                    comment.getUser().getProfilePic()
                            )
            );

        }

        response.setContent(
                comment.getContent()
        );

        response.setCreatedAt(
                comment.getCreatedAt()
        );

        response.setUpdatedAt(
                comment.getUpdatedAt()
        );

        response.setEdited(
                comment.getUpdatedAt() != null
        );

        return response;

    }

}

package com.movem.backend.social.comment.mapper;


import com.movem.backend.social.comment.dtos.response.CommentResponse;
import com.movem.backend.social.comment.entity.Comment;
import com.movem.backend.commons.BaseMapper.AbstractBaseMapper;
import org.springframework.stereotype.Component;

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
                    comment.getUser().getProfilePic()
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
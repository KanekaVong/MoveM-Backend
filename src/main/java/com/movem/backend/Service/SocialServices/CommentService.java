package com.movem.backend.Service.SocialServices;

import com.movem.backend.Dto.request.CommentRequest.CreateCommentRequest;
import com.movem.backend.Dto.request.CommentRequest.UpdateCommentRequest;
import com.movem.backend.Dto.response.CommentResponse.CommentResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface CommentService {

    CommentResponse createComment(
            String activityId,
            CreateCommentRequest request
    );

    Page<CommentResponse> getComments(
            String activityId,
            Pageable pageable
    );

    CommentResponse updateComment(
            Long commentId,
            UpdateCommentRequest request
    );

    void deleteComment(
            Long commentId
    );

    CommentResponse createWorkoutComment(
            Integer sessionId,
            CreateCommentRequest request
    );

    Page<CommentResponse> getWorkoutComments(
            Integer sessionId,
            Pageable pageable
    );
}
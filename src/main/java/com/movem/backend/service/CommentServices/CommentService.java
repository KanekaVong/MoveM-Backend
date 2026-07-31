package com.movem.backend.service.CommentServices;

import com.movem.backend.dto.request.CommentRequest.CreateCommentRequest;
import com.movem.backend.dto.request.CommentRequest.UpdateCommentRequest;
import com.movem.backend.dto.response.CommentResponse.CommentResponse;

import java.util.List;

public interface CommentService {

    CommentResponse createComment(
            String activityId,
            CreateCommentRequest request
    );

    List<CommentResponse> getComments(
            String activityId
    );

    CommentResponse updateComment(
            Long commentId,
            UpdateCommentRequest request
    );

    void deleteComment(
            Long commentId
    );

}
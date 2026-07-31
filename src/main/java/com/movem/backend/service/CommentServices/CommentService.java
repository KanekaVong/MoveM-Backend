package com.movem.backend.service.CommentServices;

import com.movem.backend.dto.request.CommentRequest.CreateCommentRequest;
import com.movem.backend.dto.request.CommentRequest.UpdateCommentRequest;
import com.movem.backend.dto.response.CommentResponse.CommentResponse;
import com.movem.backend.entity.Activity.Activity;
import com.movem.backend.entity.Comment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

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

}
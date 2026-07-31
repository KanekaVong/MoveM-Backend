package com.movem.backend.controller.CommentController;

import com.movem.backend.dto.request.CommentRequest.CreateCommentRequest;
import com.movem.backend.dto.request.CommentRequest.UpdateCommentRequest;
import com.movem.backend.dto.response.CommentResponse.CommentResponse;
import com.movem.backend.service.CommentServices.CommentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/comments")
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;

    @PostMapping("/{activityId}")
    public ResponseEntity<CommentResponse> createComment(
            @PathVariable String activityId,
            @Valid @RequestBody CreateCommentRequest request
    ) {

        return ResponseEntity.ok(
                commentService.createComment(
                        activityId,
                        request
                )
        );

    }

    @GetMapping("/{activityId}")
    public ResponseEntity<List<CommentResponse>> getComments(
            @PathVariable String activityId
    ) {

        return ResponseEntity.ok(
                commentService.getComments(activityId)
        );

    }

    @PutMapping("/{commentId}")
    public ResponseEntity<CommentResponse> updateComment(
            @PathVariable Long commentId,
            @Valid @RequestBody UpdateCommentRequest request
    ) {

        return ResponseEntity.ok(
                commentService.updateComment(
                        commentId,
                        request
                )
        );

    }

    @DeleteMapping("/{commentId}")
    public ResponseEntity<Void> deleteComment(
            @PathVariable Long commentId
    ) {

        commentService.deleteComment(commentId);

        return ResponseEntity.noContent().build();

    }

}
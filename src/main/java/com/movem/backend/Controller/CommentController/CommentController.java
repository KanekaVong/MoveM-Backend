package com.movem.backend.Controller.CommentController;

import com.movem.backend.Dto.request.CommentRequest.CreateCommentRequest;
import com.movem.backend.Dto.request.CommentRequest.UpdateCommentRequest;
import com.movem.backend.Dto.response.CommentResponse.CommentResponse;
import com.movem.backend.Service.CommentServices.CommentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
    public Page<CommentResponse> getComments(
            @PathVariable String activityId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {

        Pageable pageable =
                PageRequest.of(page, size);

        return commentService.getComments(
                activityId,
                pageable
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
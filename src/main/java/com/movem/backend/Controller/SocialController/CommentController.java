package com.movem.backend.Controller.SocialController;

import com.movem.backend.Dto.request.CommentRequest.CreateCommentRequest;
import com.movem.backend.Dto.request.CommentRequest.UpdateCommentRequest;
import com.movem.backend.Dto.response.CommentResponse.CommentResponse;
import com.movem.backend.Service.SocialServices.CommentService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/comments")
@Tag(
        name = "Social - Comments",
        description = "Commenting in Tasks and Shared Workouts"
)
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

    @PostMapping("/workouts/{sessionId}")
    public ResponseEntity<CommentResponse> createWorkoutComment(
            @PathVariable Integer sessionId,
            @Valid @RequestBody CreateCommentRequest request
    ) {
        return ResponseEntity.ok(
                commentService.createWorkoutComment(
                        sessionId,
                        request
                )
        );
    }

    @GetMapping("/workouts/{sessionId}")
    public Page<CommentResponse> getWorkoutComments(
            @PathVariable Integer sessionId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        Pageable pageable = PageRequest.of(page, size);

        return commentService.getWorkoutComments(
                sessionId,
                pageable
        );
    }

}
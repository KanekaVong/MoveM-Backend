package com.movem.backend.social.comment.controller;

import com.movem.backend.social.comment.dtos.requests.CreateCommentRequest;
import com.movem.backend.social.comment.dtos.requests.UpdateCommentRequest;
import com.movem.backend.social.comment.dtos.response.CommentResponse;
import com.movem.backend.social.friend.dtos.response.InviteResponse;
import com.movem.backend.social.friend.services.InviteService;
import com.movem.backend.social.comment.service.CommentService;
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

    @RestController
    @RequestMapping("/api/invites")
    @RequiredArgsConstructor
    public static class InviteController {

        private final InviteService inviteService;

        @PostMapping
        public ResponseEntity<InviteResponse> createInvite() {

            return ResponseEntity.ok(
                    inviteService.createInvite()
            );
        }

        @GetMapping("/{token}")
        public ResponseEntity<InviteResponse> getInvite(
                @PathVariable String token
        ) {

            return ResponseEntity.ok(
                    inviteService.getInvite(token)
            );
        }

        @PostMapping("/{token}/accept")
        public ResponseEntity<Void> acceptInvite(
                @PathVariable String token
        ) {

            inviteService.acceptInvite(
                    token
            );

            return ResponseEntity.ok().build();
        }
    }
}
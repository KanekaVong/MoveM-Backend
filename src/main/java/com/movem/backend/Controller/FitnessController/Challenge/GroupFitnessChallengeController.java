package com.movem.backend.Controller.FitnessController.Challenge;

import com.movem.backend.Dto.request.FitnessRequest.Challenge.GroupChallenge.CreateGroupFitnessChallengeFromCatalogRequest;
import com.movem.backend.Dto.request.FitnessRequest.Challenge.GroupChallenge.CreateGroupFitnessChallengeRequest;
import com.movem.backend.Dto.request.FitnessRequest.Challenge.GroupChallenge.UpdateGroupFitnessChallengeRequest;
import com.movem.backend.Dto.response.FitnessResponse.Challenge.GroupFitnessChallengeResponse;
import com.movem.backend.Dto.response.FitnessResponse.Social.SocialChallengeResponse;
import com.movem.backend.Service.FitnessServices.Challenge.GroupFitnessChallengeService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/fitness")
@Tag(
        name = "Fitness - Group Challenges",
        description = "Group Challenges Customization"
)
@RequiredArgsConstructor
public class GroupFitnessChallengeController {

    private final GroupFitnessChallengeService
            groupFitnessChallengeService;

    @PostMapping("/clubs/{clubId}/challenges")
    public ResponseEntity<GroupFitnessChallengeResponse>
    createChallenge(
            @PathVariable Integer clubId,

            @Valid
            @RequestBody
            CreateGroupFitnessChallengeRequest request
    ) {

        GroupFitnessChallengeResponse response =
                groupFitnessChallengeService.createChallenge(
                        clubId,
                        request
                );

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }

    @GetMapping("/clubs/{clubId}/challenges")
    public ResponseEntity<List<GroupFitnessChallengeResponse>>
    getClubChallenges(
            @PathVariable Integer clubId
    ) {

        return ResponseEntity.ok(
                groupFitnessChallengeService
                        .getClubChallenges(clubId)
        );
    }

    @GetMapping("/challenges/{challengeId}")
    public ResponseEntity<GroupFitnessChallengeResponse>
    getChallenge(
            @PathVariable Integer challengeId
    ) {

        return ResponseEntity.ok(
                groupFitnessChallengeService
                        .getChallenge(challengeId)
        );
    }

    @GetMapping("/challenges/my")
    public ResponseEntity<List<GroupFitnessChallengeResponse>>
    getMyCreatedChallenges() {

        return ResponseEntity.ok(
                groupFitnessChallengeService
                        .getMyCreatedChallenges()
        );
    }

    @PutMapping("/challenges/{challengeId}")
    public ResponseEntity<GroupFitnessChallengeResponse>
    updateChallenge(
            @PathVariable Integer challengeId,

            @Valid
            @RequestBody
            UpdateGroupFitnessChallengeRequest request
    ) {

        return ResponseEntity.ok(
                groupFitnessChallengeService.updateChallenge(
                        challengeId,
                        request
                )
        );
    }

    @DeleteMapping("/challenges/{challengeId}")
    public ResponseEntity<Void>
    deleteChallenge(
            @PathVariable Integer challengeId
    ) {

        groupFitnessChallengeService
                .deleteChallenge(challengeId);

        return ResponseEntity
                .noContent()
                .build();
    }

    @PostMapping(
            "/clubs/{clubId}/challenges/from-catalog/{catalogId}"
    )
    public ResponseEntity<GroupFitnessChallengeResponse>
    createChallengeFromCatalog(
            @PathVariable Integer clubId,
            @PathVariable Integer catalogId,

            @Valid
            @RequestBody
            CreateGroupFitnessChallengeFromCatalogRequest request
    ) {

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(
                        groupFitnessChallengeService
                                .createChallengeFromCatalog(
                                        clubId,
                                        catalogId,
                                        request
                                )
                );
    }

    @GetMapping("/{challengeId}/social")
    public ResponseEntity<SocialChallengeResponse> getSocialChallenge(
            @PathVariable Integer challengeId
    ) {
        return ResponseEntity.ok(
                groupFitnessChallengeService.getSocialChallenge(
                        challengeId
                )
        );
    }
}
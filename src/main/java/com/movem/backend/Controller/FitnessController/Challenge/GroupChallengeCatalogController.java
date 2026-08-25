package com.movem.backend.Controller.FitnessController.Challenge;

import com.movem.backend.Dto.request.FitnessRequest.Challenge.GroupChallenge.CreateGroupChallengeCatalogRequest;
import com.movem.backend.Dto.request.FitnessRequest.Challenge.GroupChallenge.UpdateGroupChallengeCatalogRequest;
import com.movem.backend.Dto.response.FitnessResponse.Challenge.GroupChallengeCatalogResponse;
import com.movem.backend.Service.FitnessServices.Challenge.GroupChallengeCatalogService;
import com.movem.backend.model.enums.Fitness.WorkoutType;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/fitness/group-challenge/catalog")
@Tag(
        name = "Fitness - Group Challenge Catalog",
        description = "Catalogs of Group Challenges"
)
@RequiredArgsConstructor
public class GroupChallengeCatalogController {

    private final GroupChallengeCatalogService
            groupChallengeCatalogService;

    @PostMapping
    public ResponseEntity<GroupChallengeCatalogResponse>
    createCatalogChallenge(
            @Valid
            @RequestBody
            CreateGroupChallengeCatalogRequest request
    ) {

        GroupChallengeCatalogResponse response =
                groupChallengeCatalogService
                        .createCatalogChallenge(request);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }

    @GetMapping
    public ResponseEntity<List<GroupChallengeCatalogResponse>>
    getAllCatalogChallenges() {

        return ResponseEntity.ok(
                groupChallengeCatalogService
                        .getAllCatalogChallenges()
        );
    }

    @GetMapping("/{catalogId}")
    public ResponseEntity<GroupChallengeCatalogResponse>
    getCatalogChallenge(
            @PathVariable Integer catalogId
    ) {

        return ResponseEntity.ok(
                groupChallengeCatalogService
                        .getCatalogChallenge(catalogId)
        );
    }

    @GetMapping("/workout-type/{workoutType}")
    public ResponseEntity<List<GroupChallengeCatalogResponse>>
    getCatalogChallengesByWorkoutType(
            @PathVariable WorkoutType workoutType
    ) {

        return ResponseEntity.ok(
                groupChallengeCatalogService
                        .getCatalogChallengesByWorkoutType(
                                workoutType
                        )
        );
    }

    @PutMapping("/{catalogId}")
    public ResponseEntity<GroupChallengeCatalogResponse>
    updateCatalogChallenge(
            @PathVariable Integer catalogId,

            @Valid
            @RequestBody
            UpdateGroupChallengeCatalogRequest request
    ) {

        return ResponseEntity.ok(
                groupChallengeCatalogService
                        .updateCatalogChallenge(
                                catalogId,
                                request
                        )
        );
    }

    @DeleteMapping("/{catalogId}")
    public ResponseEntity<Void>
    deleteCatalogChallenge(
            @PathVariable Integer catalogId
    ) {

        groupChallengeCatalogService
                .deleteCatalogChallenge(catalogId);

        return ResponseEntity.noContent().build();
    }
}
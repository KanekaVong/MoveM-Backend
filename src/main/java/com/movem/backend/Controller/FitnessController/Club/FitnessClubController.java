package com.movem.backend.Controller.FitnessController.Club;

import com.movem.backend.Dto.request.FitnessRequest.Club.CreateFitnessClubRequest;
import com.movem.backend.Dto.request.FitnessRequest.Club.UpdateFitnessClubRequest;
import com.movem.backend.Dto.response.FitnessResponse.Club.FitnessClubResponse;
import com.movem.backend.Service.FitnessServices.Club.FitnessClubService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/fitness/clubs")
@RequiredArgsConstructor
public class FitnessClubController {

    private final FitnessClubService fitnessClubService;

    @PostMapping
    public ResponseEntity<FitnessClubResponse> createClub(
            @Valid @RequestBody CreateFitnessClubRequest request
    ) {

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(
                        fitnessClubService.createClub(request)
                );
    }

    @GetMapping("/{clubId}")
    public ResponseEntity<FitnessClubResponse> getClub(
            @PathVariable Integer clubId
    ) {

        return ResponseEntity.ok(
                fitnessClubService.getClub(clubId)
        );
    }

    @GetMapping("/join/{joinToken}")
    public ResponseEntity<FitnessClubResponse> getClubByJoinToken(
            @PathVariable String joinToken
    ) {

        return ResponseEntity.ok(
                fitnessClubService.getClubByJoinToken(
                        joinToken
                )
        );
    }

    @GetMapping("/my")
    public ResponseEntity<List<FitnessClubResponse>> getMyClubs() {

        return ResponseEntity.ok(
                fitnessClubService.getMyClubs()
        );
    }

    @GetMapping("/public")
    public ResponseEntity<List<FitnessClubResponse>> getPublicClubs() {

        return ResponseEntity.ok(
                fitnessClubService.getPublicClubs()
        );
    }

    @PutMapping("/{clubId}")
    public ResponseEntity<FitnessClubResponse> updateClub(
            @PathVariable Integer clubId,

            @Valid
            @RequestBody
            UpdateFitnessClubRequest request
    ) {

        return ResponseEntity.ok(
                fitnessClubService.updateClub(
                        clubId,
                        request
                )
        );
    }

    @DeleteMapping("/{clubId}")
    public ResponseEntity<Void> deleteClub(
            @PathVariable Integer clubId
    ) {

        fitnessClubService.deleteClub(clubId);

        return ResponseEntity
                .noContent()
                .build();
    }
}
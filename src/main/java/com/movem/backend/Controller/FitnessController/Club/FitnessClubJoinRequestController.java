package com.movem.backend.Controller.FitnessController.Club;

import com.movem.backend.Dto.response.FitnessResponse.Club.FitnessClubJoinRequestResponse;
import com.movem.backend.Service.FitnessServices.Club.FitnessClubJoinRequestService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/fitness/clubs")
@Tag(
        name = "Fitness - Club",
        description = "Fitness Club"
)
@RequiredArgsConstructor
public class FitnessClubJoinRequestController {

    private final FitnessClubJoinRequestService
            fitnessClubJoinRequestService;

    @PostMapping("/{clubId}/join-request")
    public ResponseEntity<FitnessClubJoinRequestResponse>
    requestToJoin(
            @PathVariable Integer clubId
    ) {

        return ResponseEntity.ok(
                fitnessClubJoinRequestService
                        .requestToJoin(clubId)
        );
    }

    @GetMapping("/{clubId}/join-requests")
    public ResponseEntity<List<FitnessClubJoinRequestResponse>>
    getPendingRequests(
            @PathVariable Integer clubId
    ) {

        return ResponseEntity.ok(
                fitnessClubJoinRequestService
                        .getPendingRequests(clubId)
        );
    }

    @PostMapping(
            "/{clubId}/join-requests/{requestId}/approve"
    )
    public ResponseEntity<FitnessClubJoinRequestResponse>
    approveRequest(
            @PathVariable Integer clubId,
            @PathVariable Long requestId
    ) {

        return ResponseEntity.ok(
                fitnessClubJoinRequestService
                        .approveRequest(
                                clubId,
                                requestId
                        )
        );
    }

    @PostMapping(
            "/{clubId}/join-requests/{requestId}/reject"
    )
    public ResponseEntity<FitnessClubJoinRequestResponse>
    rejectRequest(
            @PathVariable Integer clubId,
            @PathVariable Long requestId
    ) {

        return ResponseEntity.ok(
                fitnessClubJoinRequestService
                        .rejectRequest(
                                clubId,
                                requestId
                        )
        );
    }

    @DeleteMapping("/join-requests/{requestId}")
    public ResponseEntity<Void>
    cancelRequest(
            @PathVariable Long requestId
    ) {

        fitnessClubJoinRequestService
                .cancelRequest(requestId);

        return ResponseEntity.noContent().build();
    }

    @GetMapping("/join-requests/my")
    public ResponseEntity<List<FitnessClubJoinRequestResponse>>
    getMyRequests() {

        return ResponseEntity.ok(
                fitnessClubJoinRequestService
                        .getMyRequests()
        );
    }
}
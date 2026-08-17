package com.movem.backend.Controller.FitnessController.Club;

import com.movem.backend.Dto.request.FitnessRequest.Club.AddFitnessClubMemberRequest;
import com.movem.backend.Dto.request.FitnessRequest.Club.UpdateFitnessClubMemberRoleRequest;
import com.movem.backend.Dto.response.FitnessResponse.Club.FitnessClubMemberResponse;
import com.movem.backend.Service.FitnessServices.Club.FitnessClubMemberService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/fitness/clubs")
@RequiredArgsConstructor
public class FitnessClubMemberController {

    private final FitnessClubMemberService fitnessClubMemberService;

    @PostMapping("/{clubId}/join")
    public ResponseEntity<FitnessClubMemberResponse>
    joinClub(
            @PathVariable Integer clubId
    ) {

        return ResponseEntity.ok(
                fitnessClubMemberService
                        .addCurrentUserAsMember(clubId)
        );
    }

    @PostMapping("/{clubId}/members")
    public ResponseEntity<FitnessClubMemberResponse>
    addMember(
            @PathVariable Integer clubId,

            @Valid
            @RequestBody
            AddFitnessClubMemberRequest request
    ) {

        return ResponseEntity.ok(
                fitnessClubMemberService
                        .addMember(
                                clubId,
                                request
                        )
        );
    }

    @GetMapping("/{clubId}/members")
    public ResponseEntity<List<FitnessClubMemberResponse>>
    getClubMembers(
            @PathVariable Integer clubId
    ) {

        return ResponseEntity.ok(
                fitnessClubMemberService
                        .getClubMembers(clubId)
        );
    }

    @GetMapping("/{clubId}/members/{userId}")
    public ResponseEntity<FitnessClubMemberResponse>
    getMember(
            @PathVariable Integer clubId,
            @PathVariable Integer userId
    ) {

        return ResponseEntity.ok(
                fitnessClubMemberService
                        .getMember(
                                clubId,
                                userId
                        )
        );
    }

    @PutMapping("/{clubId}/members/{userId}/role")
    public ResponseEntity<FitnessClubMemberResponse>
    updateMemberRole(
            @PathVariable Integer clubId,
            @PathVariable Integer userId,

            @Valid
            @RequestBody
            UpdateFitnessClubMemberRoleRequest request
    ) {

        return ResponseEntity.ok(
                fitnessClubMemberService
                        .updateMemberRole(
                                clubId,
                                userId,
                                request
                        )
        );
    }

    @DeleteMapping("/{clubId}/members/{userId}")
    public ResponseEntity<Void>
    removeMember(
            @PathVariable Integer clubId,
            @PathVariable Integer userId
    ) {

        fitnessClubMemberService
                .removeMember(
                        clubId,
                        userId
                );

        return ResponseEntity
                .noContent()
                .build();
    }
}
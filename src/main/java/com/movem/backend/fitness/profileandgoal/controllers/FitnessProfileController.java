package com.movem.backend.fitness.profileandgoal.controllers;

import com.movem.backend.fitness.profileandgoal.dtos.requests.CreateFitnessProfileRequest;
import com.movem.backend.fitness.profileandgoal.dtos.requests.UpdateFitnessProfileRequest;
import com.movem.backend.fitness.profileandgoal.dtos.responses.FitnessProfileResponse;
import com.movem.backend.fitness.profileandgoal.services.FitnessProfileService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/fitness/profile")
@Tag(
        name = "Fitness - Profile",
        description = "Fitness Profile"
)
@RequiredArgsConstructor
public class FitnessProfileController {

    private final FitnessProfileService fitnessProfileService;


    @PostMapping
    public ResponseEntity<FitnessProfileResponse> createProfile(
            @Valid @RequestBody CreateFitnessProfileRequest request
    ) {

        FitnessProfileResponse response =
                fitnessProfileService.createProfile(request);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }


    @GetMapping
    public ResponseEntity<FitnessProfileResponse> getMyProfile() {

        return ResponseEntity.ok(
                fitnessProfileService.getMyProfile()
        );
    }


    @PutMapping
    public ResponseEntity<FitnessProfileResponse> updateProfile(
            @Valid @RequestBody UpdateFitnessProfileRequest request
    ) {

        return ResponseEntity.ok(
                fitnessProfileService.updateProfile(request)
        );
    }


    @DeleteMapping
    public ResponseEntity<Void> deleteProfile() {

        fitnessProfileService.deleteProfile();

        return ResponseEntity.noContent().build();
    }
}
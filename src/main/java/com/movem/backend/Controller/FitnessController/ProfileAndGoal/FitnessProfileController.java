package com.movem.backend.Controller.FitnessController.ProfileAndGoal;

import com.movem.backend.Dto.request.FitnessRequest.ProfileAndGoal.CreateFitnessProfileRequest;
import com.movem.backend.Dto.request.FitnessRequest.ProfileAndGoal.UpdateFitnessProfileRequest;
import com.movem.backend.Dto.response.FitnessResponse.ProfileAndGoal.FitnessProfileResponse;
import com.movem.backend.Service.FitnessServices.ProfileAndGoal.FitnessProfileService;
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
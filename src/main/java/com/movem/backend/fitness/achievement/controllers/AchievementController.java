package com.movem.backend.fitness.achievement.controllers;

import com.movem.backend.fitness.achievement.dtos.responses.AchievementResponse;
import com.movem.backend.fitness.achievement.dtos.responses.UserAchievementResponse;
import com.movem.backend.fitness.achievement.services.AchievementService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/fitness/achievements")
@Tag(
        name = "Achievements"
)
@RequiredArgsConstructor
public class AchievementController {

    private final AchievementService achievementService;

    @GetMapping("/me/count")
    public ResponseEntity<Long> getMyAchievementCount() {

        return ResponseEntity.ok(
                achievementService.getMyAchievementCount()
        );
    }

    @GetMapping("/me")
    public ResponseEntity<List<UserAchievementResponse>> getMyAchievements() {

        return ResponseEntity.ok(
                achievementService.getMyAchievements()
        );
    }

    @GetMapping
    public ResponseEntity<List<AchievementResponse>> getAllAchievements() {
        return ResponseEntity.ok(
                achievementService.getAllAchievements()
        );
    }
}
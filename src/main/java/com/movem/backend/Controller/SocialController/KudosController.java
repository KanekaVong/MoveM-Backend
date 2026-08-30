package com.movem.backend.Controller.SocialController;

import com.movem.backend.Service.SocialServices.KudosService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/fitness/workouts")
@Tag(
        name = "Fitness - Kudos",
        description = "Kudos/Like Your Friend's Post"
)
@RequiredArgsConstructor
public class KudosController {

    private final KudosService workoutKudosService;

    @PostMapping("/{sessionId}/kudos")
    public ResponseEntity<Void> giveKudos(
            @PathVariable Integer sessionId
    ) {

        workoutKudosService.giveKudos(sessionId);

        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{sessionId}/kudos")
    public ResponseEntity<Void> removeKudos(
            @PathVariable Integer sessionId
    ) {

        workoutKudosService.removeKudos(sessionId);

        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{sessionId}/kudos/count")
    public ResponseEntity<Long> getKudosCount(
            @PathVariable Integer sessionId
    ) {

        return ResponseEntity.ok(
                workoutKudosService.getKudosCount(sessionId)
        );
    }

    @GetMapping("/{sessionId}/kudos/me")
    public ResponseEntity<Boolean> hasGivenKudos(
            @PathVariable Integer sessionId
    ) {

        return ResponseEntity.ok(
                workoutKudosService.hasGivenKudos(sessionId)
        );
    }
}
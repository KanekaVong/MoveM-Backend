package com.movem.backend.Controller.ActivityController;

import com.movem.backend.Service.SharedServices.ActivityService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/activities")
@Tag(
        name = "Activity - Base"
)
@RequiredArgsConstructor
public class ActivityController {

    private final ActivityService activityService;

    @DeleteMapping("/{activityId}/permanent")
    public ResponseEntity<Void> permanentlyDeleteActivity(
            @PathVariable String activityId
    ) {

        activityService.permanentlyDeleteActivity(activityId);

        return ResponseEntity.noContent().build();
    }

}

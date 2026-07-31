package com.movem.backend.controller.ActivityController;

import com.movem.backend.service.SharedServices.ActivityService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/activities")
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

package com.movem.backend.Controller.TripControllers;

import com.movem.backend.Dto.request.TripRequest.Create.CreateTripBookmarkRequest;
import com.movem.backend.Dto.response.TripResponses.TripBookmarkResponse;
import com.movem.backend.Service.TripServices.TripBookmarkService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/trip-bookmarks")
@Tag( name = "Trip - Trip Bookmark",
        description = "Create trip, add collaborators, plan trips seamlessly")
@RequiredArgsConstructor
public class TripBookmarkController {

    private final TripBookmarkService tripBookmarkService;

    @PostMapping
    public ResponseEntity<TripBookmarkResponse> addBookmark(@Valid @RequestBody CreateTripBookmarkRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(tripBookmarkService.addBookmark(request));
    }

    @GetMapping
    public ResponseEntity<List<TripBookmarkResponse>> getMyBookmarks() {
        return ResponseEntity.ok(tripBookmarkService.getMyBookmarks());
    }

    @DeleteMapping("/{bookmarkId}")
    public ResponseEntity<Void> removeBookmark(@PathVariable Integer bookmarkId) {
        tripBookmarkService.removeBookmark(bookmarkId);
        return ResponseEntity.noContent().build();
    }
}

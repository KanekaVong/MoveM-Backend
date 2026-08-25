package com.movem.backend.Service.Implement.TripServices;

import com.movem.backend.Dto.request.TripRequest.Create.CreateTripBookmarkRequest;
import com.movem.backend.Dto.response.TripResponses.TripBookmarkResponse;
import com.movem.backend.Entity.Auth.User;
import com.movem.backend.Entity.Trip.TripBookmark;
import com.movem.backend.Exception.ResourceNotFoundException;
import com.movem.backend.Repository.TripRepositories.TripBookmarkRepository;
import com.movem.backend.Service.AuthServices.CurrentUserService;
import com.movem.backend.Service.TripServices.TripBookmarkService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class TripBookmarkServiceImpl implements TripBookmarkService {

    private final TripBookmarkRepository tripBookmarkRepository;
    private final CurrentUserService currentUserService;

    @Override
    public TripBookmarkResponse addBookmark(CreateTripBookmarkRequest request) {

        User user = currentUserService.getCurrentUser();

        TripBookmark bookmark = new TripBookmark();
        bookmark.setUser(user);
        bookmark.setLocationName(request.getLocationName());
        bookmark.setLocationAddress(request.getLocationAddress());
        bookmark.setLat(request.getLat());
        bookmark.setLng(request.getLng());
        bookmark.setGooglePlaceId(request.getGooglePlaceId());
        bookmark.setCreatedAt(LocalDateTime.now());

        tripBookmarkRepository.save(bookmark);

        return toResponse(bookmark);
    }

    @Override
    public List<TripBookmarkResponse> getMyBookmarks() {

        User user = currentUserService.getCurrentUser();

        return tripBookmarkRepository.findByUserOrderByCreatedAtDesc(user).stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    public void removeBookmark(Integer bookmarkId) {

        User user = currentUserService.getCurrentUser();

        TripBookmark bookmark = tripBookmarkRepository.findByIdAndUser(bookmarkId, user)
                .orElseThrow(() -> new ResourceNotFoundException("Bookmark not found: " + bookmarkId));

        tripBookmarkRepository.delete(bookmark);
    }

    private TripBookmarkResponse toResponse(TripBookmark bookmark) {
        return TripBookmarkResponse.builder()
                .id(bookmark.getId())
                .locationName(bookmark.getLocationName())
                .locationAddress(bookmark.getLocationAddress())
                .lat(bookmark.getLat())
                .lng(bookmark.getLng())
                .googlePlaceId(bookmark.getGooglePlaceId())
                .createdAt(bookmark.getCreatedAt())
                .build();
    }
}

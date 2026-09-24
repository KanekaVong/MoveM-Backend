package com.movem.backend.trip.services;

import com.movem.backend.trip.dtos.requests.Create.CreateTripBookmarkRequest;
import com.movem.backend.trip.dtos.responses.TripBookmarkResponse;

import java.util.List;

public interface TripBookmarkService {

    TripBookmarkResponse addBookmark(CreateTripBookmarkRequest request);

    List<TripBookmarkResponse> getMyBookmarks();

    void removeBookmark(Integer bookmarkId);
}

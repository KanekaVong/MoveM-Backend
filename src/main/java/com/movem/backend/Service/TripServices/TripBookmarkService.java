package com.movem.backend.Service.TripServices;

import com.movem.backend.Dto.request.TripRequest.Create.CreateTripBookmarkRequest;
import com.movem.backend.Dto.response.TripResponses.TripBookmarkResponse;

import java.util.List;

public interface TripBookmarkService {

    TripBookmarkResponse addBookmark(CreateTripBookmarkRequest request);

    List<TripBookmarkResponse> getMyBookmarks();

    void removeBookmark(Integer bookmarkId);
}

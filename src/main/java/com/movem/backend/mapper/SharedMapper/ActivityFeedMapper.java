package com.movem.backend.mapper.SharedMapper;

import com.movem.backend.dto.response.ActivityFeedResponse;
import com.movem.backend.entity.FeedsAndLogs.ActivityFeed;
import org.springframework.stereotype.Component;

import java.util.Base64;

@Component
public class ActivityFeedMapper {

    public ActivityFeedResponse toResponse(
            ActivityFeed feed
    ) {

        ActivityFeedResponse response =
                new ActivityFeedResponse();

        response.setId(feed.getId());

        response.setActivityId(
                feed.getActivity().getId()
        );

        response.setUserId(
                feed.getUser().getId()
        );

        response.setUsername(
                feed.getUser().getUsername()
        );

        response.setFirstname(
                feed.getUser().getFirstname()
        );

        response.setLastname(
                feed.getUser().getLastname()
        );

        if (feed.getUser().getProfilePic() != null) {

            response.setProfilePic(
                    Base64.getEncoder().encodeToString(
                            feed.getUser().getProfilePic()
                    )
            );

        }

        response.setEventType(
                feed.getEventType()
        );

        response.setMessage(
                feed.getMessage()
        );

        response.setReferenceId(
                feed.getReferenceId()
        );

        response.setCreatedAt(
                feed.getCreatedAt()
        );

        return response;

    }

}
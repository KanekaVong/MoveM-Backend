package com.movem.backend.shared.historyandlogs.activityfeed.mapper;

import com.movem.backend.shared.historyandlogs.activityfeed.dtos.responses.ActivityFeedResponse;
import com.movem.backend.shared.historyandlogs.activityfeed.entities.ActivityFeed;
import com.movem.backend.commons.BaseMapper.AbstractBaseMapper;
import lombok.*;
import org.springframework.stereotype.Component;

@Getter
@Setter
@Builder
@Component
public class ActivityFeedMapper
        extends AbstractBaseMapper<ActivityFeed, ActivityFeedResponse> {

    @Override
    public ActivityFeedResponse toResponse(ActivityFeed feed) {

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
                    feed.getUser().getProfilePic()
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
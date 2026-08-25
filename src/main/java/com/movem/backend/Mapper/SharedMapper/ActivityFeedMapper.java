package com.movem.backend.Mapper.SharedMapper;

import com.movem.backend.Dto.response.ActivityFeedResponse;
import com.movem.backend.Entity.FeedsAndLogs.ActivityFeed;
import com.movem.backend.Mapper.BaseMapper.AbstractBaseMapper;
import lombok.*;
import org.springframework.stereotype.Component;

import java.util.Base64;

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
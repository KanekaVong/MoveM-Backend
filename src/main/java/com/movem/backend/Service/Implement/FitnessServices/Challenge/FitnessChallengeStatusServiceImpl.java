package com.movem.backend.Service.Implement.FitnessServices.Challenge;

import com.movem.backend.Entity.Activity.Activity;
import com.movem.backend.Entity.Fitness.Challenge.GroupFitnessChallenge;
import com.movem.backend.Service.Event.Factory.Fitness.FitnessChallengeEventFactory;
import com.movem.backend.Service.Event.FeatureEventTrackingService;
import com.movem.backend.model.enums.Activity.ActivityStatus;
import com.movem.backend.model.enums.Fitness.FitnessChallengeStatus;
import com.movem.backend.Repository.FitnessRepository.Challenge.GroupFitnessChallengeRepository;
import com.movem.backend.Repository.SharedRepository.ActivityRepository;
import com.movem.backend.Service.FitnessServices.Challenge.FitnessChallengeStatusService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

    @Service
    @RequiredArgsConstructor
    @Transactional
    public class FitnessChallengeStatusServiceImpl
            implements FitnessChallengeStatusService {

        private final FeatureEventTrackingService featureEventTrackingService;
        private final FitnessChallengeEventFactory fitnessChallengeEventFactory;
        private final GroupFitnessChallengeRepository groupFitnessChallengeRepository;
        private final ActivityRepository activityRepository;


        @Override
        @Scheduled(fixedRate = 20000)
        public void updateChallengeStatuses() {

            LocalDateTime now =
                    LocalDateTime.now();

            updateUpcomingChallenges(now);

            updateInProgressChallenges(now);
        }


        private void updateUpcomingChallenges(
                LocalDateTime now
        ) {

            List<GroupFitnessChallenge> challenges =
                    groupFitnessChallengeRepository
                            .findAll()
                            .stream()
                            .filter(challenge ->
                                    challenge.getStatus()
                                            == FitnessChallengeStatus.UPCOMING
                            )
                            .filter(challenge ->
                                    !now.isBefore(
                                            challenge.getStartAt()
                                    )
                            )
                            .toList();


            for (GroupFitnessChallenge challenge : challenges) {

                challenge.setStatus(
                        FitnessChallengeStatus.IN_PROGRESS
                );

                updateActivityStatus(
                        challenge.getActivity(),
                        ActivityStatus.IN_PROGRESS
                );

                challenge.setUpdatedAt(now);


                featureEventTrackingService.handle(
                        fitnessChallengeEventFactory.challengeStarted(
                                challenge
                        )
                );
            }


            if (!challenges.isEmpty()) {

                groupFitnessChallengeRepository.saveAll(
                        challenges
                );
            }
        }


        private void updateInProgressChallenges(
                LocalDateTime now
        ) {

            List<GroupFitnessChallenge> challenges =
                    groupFitnessChallengeRepository
                            .findAll()
                            .stream()
                            .filter(challenge ->
                                    challenge.getStatus()
                                            == FitnessChallengeStatus.IN_PROGRESS
                            )
                            .filter(challenge ->
                                    !now.isBefore(
                                            challenge.getEndAt()
                                    )
                            )
                            .toList();


            for (GroupFitnessChallenge challenge : challenges) {

                challenge.setStatus(
                        FitnessChallengeStatus.COMPLETE
                );

                updateActivityStatus(
                        challenge.getActivity(),
                        ActivityStatus.COMPLETE
                );

                challenge.setUpdatedAt(now);

                featureEventTrackingService.handle(
                        fitnessChallengeEventFactory.challengeCompleted(
                                challenge
                        )
                );
            }


            if (!challenges.isEmpty()) {

                groupFitnessChallengeRepository.saveAll(
                        challenges
                );
            }
        }


        private void updateActivityStatus(
                Activity activity,
                ActivityStatus status
        ) {

            if (activity == null) {
                return;
            }

            activity.setStatus(status);

            activity.setUpdatedAt(
                    LocalDateTime.now()
            );

            activityRepository.save(activity);
        }
    }



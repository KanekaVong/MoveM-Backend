package com.movem.backend.trip.repositories;

import com.movem.backend.authentication.entities.User;
import com.movem.backend.trip.entities.TripBookmark;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface TripBookmarkRepository extends JpaRepository<TripBookmark, Integer> {

    List<TripBookmark> findByUserOrderByCreatedAtDesc(User user);

    Optional<TripBookmark> findByIdAndUser(Integer id, User user);
}

package com.movem.backend.Repository.TripRepositories;

import com.movem.backend.Entity.Auth.User;
import com.movem.backend.Entity.Trip.TripBookmark;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface TripBookmarkRepository extends JpaRepository<TripBookmark, Integer> {

    List<TripBookmark> findByUserOrderByCreatedAtDesc(User user);

    Optional<TripBookmark> findByIdAndUser(Integer id, User user);
}

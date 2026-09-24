package com.movem.backend.fitness.achievement.repositories;

import com.movem.backend.fitness.achievement.entities.Achievement;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AchievementRepository
        extends JpaRepository<Achievement, Integer> {

    List<Achievement> findAllByOrderByIdAsc();


}
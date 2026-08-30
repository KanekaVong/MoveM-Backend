package com.movem.backend.Repository.FitnessRepository.Achievement;

import com.movem.backend.Entity.Achievement.Achievement;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AchievementRepository
        extends JpaRepository<Achievement, Integer> {

    List<Achievement> findAllByOrderByIdAsc();


}
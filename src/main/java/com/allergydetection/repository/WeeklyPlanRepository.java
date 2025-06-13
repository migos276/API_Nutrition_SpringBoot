package com.allergydetection.repository;

import com.allergydetection.entity.WeeklyPlan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface WeeklyPlanRepository extends JpaRepository<WeeklyPlan, Long> {
    List<WeeklyPlan> findByUserIdOrderByDayOfWeekAscMealType(Long userId);
    
    @Query("SELECT wp FROM WeeklyPlan wp WHERE wp.user.id = :userId AND wp.weekStartDate = :weekStartDate ORDER BY wp.dayOfWeek ASC, wp.mealType ASC")
    List<WeeklyPlan> findByUserIdAndWeekStartDate(@Param("userId") Long userId, @Param("weekStartDate") LocalDate weekStartDate);
}
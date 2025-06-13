package com.allergydetection.repository;

import com.allergydetection.entity.Meal;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface MealRepository extends JpaRepository<Meal, Long> {
    List<Meal> findByUserIdOrderByMealTimeDesc(Long userId);
    
    @Query("SELECT m FROM Meal m WHERE m.user.id = :userId AND m.mealTime BETWEEN :startDate AND :endDate ORDER BY m.mealTime DESC")
    List<Meal> findByUserIdAndMealTimeBetween(@Param("userId") Long userId, 
                                             @Param("startDate") LocalDateTime startDate, 
                                             @Param("endDate") LocalDateTime endDate);
    
    @Query("SELECT m FROM Meal m WHERE m.user.id = :userId AND m.food.id = :foodId AND m.mealTime BETWEEN :startDate AND :endDate")
    List<Meal> findByUserIdAndFoodIdAndMealTimeBetween(@Param("userId") Long userId, 
                                                      @Param("foodId") Long foodId,
                                                      @Param("startDate") LocalDateTime startDate, 
                                                      @Param("endDate") LocalDateTime endDate);
}
package com.allergydetection.repository;

import com.allergydetection.entity.FoodImage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface FoodImageRepository extends JpaRepository<FoodImage, Long> {
    List<FoodImage> findByFoodIdOrderByIsPrimaryDescCreatedAtDesc(Long foodId);
    
    Optional<FoodImage> findByFoodIdAndIsPrimary(Long foodId, Boolean isPrimary);
    
    @Modifying
    @Query("UPDATE FoodImage fi SET fi.isPrimary = false WHERE fi.food.id = :foodId")
    void resetPrimaryImages(@Param("foodId") Long foodId);
}
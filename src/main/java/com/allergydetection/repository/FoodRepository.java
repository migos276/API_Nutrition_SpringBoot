package com.allergydetection.repository;

import com.allergydetection.entity.Food;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FoodRepository extends JpaRepository<Food, Long> {
    List<Food> findByIsBaseFood(Boolean isBaseFood);
    
    @Query("SELECT f FROM Food f WHERE f.name LIKE %:query% OR f.category LIKE %:query% OR f.ingredients LIKE %:query%")
    List<Food> searchFoods(@Param("query") String query);
    
    List<Food> findByCategory(String category);
}
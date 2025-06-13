package com.allergydetection.repository;

import com.allergydetection.entity.BuffetFood;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BuffetFoodRepository extends JpaRepository<BuffetFood, Long> {
    List<BuffetFood> findByBuffetEventId(Long buffetEventId);
}
package com.allergydetection.repository;

import com.allergydetection.entity.Symptom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface SymptomRepository extends JpaRepository<Symptom, Long> {
    List<Symptom> findByUserIdOrderByOccurrenceTimeDesc(Long userId);
    
    @Query("SELECT s FROM Symptom s WHERE s.user.id = :userId AND s.occurrenceTime BETWEEN :startDate AND :endDate ORDER BY s.occurrenceTime DESC")
    List<Symptom> findByUserIdAndOccurrenceTimeBetween(@Param("userId") Long userId, 
                                                      @Param("startDate") LocalDateTime startDate, 
                                                      @Param("endDate") LocalDateTime endDate);
    
    List<Symptom> findByUserIdAndSymptomType(Long userId, String symptomType);
}
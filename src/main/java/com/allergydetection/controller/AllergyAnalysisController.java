package com.allergydetection.controller;

import com.allergydetection.service.AllergyDetectionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api")
@Tag(name = "Allergy Analysis", description = "APIs for allergy detection and analysis")
public class AllergyAnalysisController {

    @Autowired
    private AllergyDetectionService allergyDetectionService;

    @GetMapping("/users/{userId}/allergy-analysis")
    @Operation(summary = "Analyze potential allergies for a user")
    public ResponseEntity<Map<String, Object>> analyzeAllergies(
            @PathVariable Long userId,
            @RequestParam(defaultValue = "30") double threshold) {
        
        List<AllergyDetectionService.PotentialAllergy> potentialAllergies = 
                allergyDetectionService.detectPotentialAllergies(userId, threshold);
        
        Map<String, Object> response = new HashMap<>();
        response.put("user_id", userId);
        response.put("analysis_date", LocalDateTime.now());
        response.put("threshold_used", threshold);
        response.put("potential_allergies", potentialAllergies);
        response.put("total_detected", potentialAllergies.size());
        
        return ResponseEntity.ok(response);
    }

    @GetMapping("/users/{userId}/food-risk/{foodId}")
    @Operation(summary = "Calculate risk score for a specific food")
    public ResponseEntity<AllergyDetectionService.FoodRiskScore> getFoodRiskScore(
            @PathVariable Long userId,
            @PathVariable Long foodId,
            @RequestParam(defaultValue = "30") int days) {
        
        AllergyDetectionService.FoodRiskScore riskScore = 
                allergyDetectionService.getFoodRiskScore(userId, foodId, days);
        
        return ResponseEntity.ok(riskScore);
    }
}
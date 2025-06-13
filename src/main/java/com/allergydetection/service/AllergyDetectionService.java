package com.allergydetection.service;

import com.allergydetection.dto.MealDto;
import com.allergydetection.dto.SymptomDto;
import com.allergydetection.entity.Food;
import com.allergydetection.exception.ResourceNotFoundException;
import com.allergydetection.repository.FoodRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
public class AllergyDetectionService {

    @Autowired
    private MealService mealService;

    @Autowired
    private SymptomService symptomService;

    @Autowired
    private FoodRepository foodRepository;

    public double calculateAllergyScore(Long userId, Long foodId, int daysBack) {
        LocalDateTime endDate = LocalDateTime.now();
        LocalDateTime startDate = endDate.minusDays(daysBack);

        // Get meals containing this food
        List<MealDto> foodMeals = mealService.getUserMealsByFood(userId, foodId, startDate, endDate);
        
        if (foodMeals.isEmpty()) {
            return 0.0;
        }

        // Get symptoms in the same period
        List<SymptomDto> symptoms = symptomService.getUserSymptoms(userId, startDate, endDate);

        int symptomAfterFoodCount = 0;
        int totalConsumptions = foodMeals.size();

        for (MealDto meal : foodMeals) {
            LocalDateTime mealTime = meal.getMealTime();

            // Look for symptoms 2 hours to 48 hours after the meal
            boolean hasSymptomAfterMeal = symptoms.stream().anyMatch(symptom -> {
                LocalDateTime symptomTime = symptom.getOccurrenceTime();
                Duration timeDiff = Duration.between(mealTime, symptomTime);
                
                return timeDiff.toHours() >= 2 && timeDiff.toHours() <= 48;
            });

            if (hasSymptomAfterMeal) {
                symptomAfterFoodCount++;
            }
        }

        // Calculate risk score
        if (totalConsumptions == 0) {
            return 0.0;
        }

        double riskScore = ((double) symptomAfterFoodCount / totalConsumptions) * 100;
        return Math.round(riskScore * 100.0) / 100.0; // Round to 2 decimal places
    }

    public List<PotentialAllergy> detectPotentialAllergies(Long userId, double threshold) {
        List<Food> foods = foodRepository.findAll();
        List<PotentialAllergy> potentialAllergies = new ArrayList<>();

        for (Food food : foods) {
            double score = calculateAllergyScore(userId, food.getId(), 30);

            if (score >= threshold) {
                PotentialAllergy allergy = new PotentialAllergy();
                allergy.setFoodId(food.getId());
                allergy.setFoodName(food.getName());
                allergy.setRiskScore(score);
                allergy.setRecommendation("Éviter cet aliment et consulter un médecin");
                
                potentialAllergies.add(allergy);
            }
        }

        return potentialAllergies.stream()
                .sorted((a, b) -> Double.compare(b.getRiskScore(), a.getRiskScore()))
                .collect(Collectors.toList());
    }

    public FoodRiskScore getFoodRiskScore(Long userId, Long foodId, int daysBack) {
        Food food = foodRepository.findById(foodId)
                .orElseThrow(() -> new ResourceNotFoundException("Food not found with id: " + foodId));

        double score = calculateAllergyScore(userId, foodId, daysBack);
        
        String riskLevel;
        if (score >= 30) {
            riskLevel = "Élevé";
        } else if (score >= 15) {
            riskLevel = "Modéré";
        } else {
            riskLevel = "Faible";
        }

        FoodRiskScore riskScore = new FoodRiskScore();
        riskScore.setUserId(userId);
        riskScore.setFoodId(foodId);
        riskScore.setFoodName(food.getName());
        riskScore.setRiskScore(score);
        riskScore.setDaysAnalyzed(daysBack);
        riskScore.setRiskLevel(riskLevel);

        return riskScore;
    }

    // Inner classes for response DTOs
    public static class PotentialAllergy {
        private Long foodId;
        private String foodName;
        private double riskScore;
        private String recommendation;

        // Getters and Setters
        public Long getFoodId() {
            return foodId;
        }

        public void setFoodId(Long foodId) {
            this.foodId = foodId;
        }

        public String getFoodName() {
            return foodName;
        }

        public void setFoodName(String foodName) {
            this.foodName = foodName;
        }

        public double getRiskScore() {
            return riskScore;
        }

        public void setRiskScore(double riskScore) {
            this.riskScore = riskScore;
        }

        public String getRecommendation() {
            return recommendation;
        }

        public void setRecommendation(String recommendation) {
            this.recommendation = recommendation;
        }
    }

    public static class FoodRiskScore {
        private Long userId;
        private Long foodId;
        private String foodName;
        private double riskScore;
        private int daysAnalyzed;
        private String riskLevel;

        // Getters and Setters
        public Long getUserId() {
            return userId;
        }

        public void setUserId(Long userId) {
            this.userId = userId;
        }

        public Long getFoodId() {
            return foodId;
        }

        public void setFoodId(Long foodId) {
            this.foodId = foodId;
        }

        public String getFoodName() {
            return foodName;
        }

        public void setFoodName(String foodName) {
            this.foodName = foodName;
        }

        public double getRiskScore() {
            return riskScore;
        }

        public void setRiskScore(double riskScore) {
            this.riskScore = riskScore;
        }

        public int getDaysAnalyzed() {
            return daysAnalyzed;
        }

        public void setDaysAnalyzed(int daysAnalyzed) {
            this.daysAnalyzed = daysAnalyzed;
        }

        public String getRiskLevel() {
            return riskLevel;
        }

        public void setRiskLevel(String riskLevel) {
            this.riskLevel = riskLevel;
        }
    }
}
package com.allergydetection.controller;

import com.allergydetection.service.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api")
@Tag(name = "Dashboard & Analytics", description = "Dashboard and analytics endpoints")
public class DashboardController {
    @Autowired
    private MealService mealService;

    @Autowired
    private SymptomService symptomService;

    @Autowired
    private AllergyDetectionService allergyDetectionService;

    @Autowired
    private WeeklyPlanService weeklyPlanService;

    @Autowired
    private BuffetService buffetService;

    @GetMapping("/users/{userId}/dashboard")
    @Operation(summary = "Get user dashboard with comprehensive statistics")
    public ResponseEntity<Map<String, Object>> getUserDashboard(
            @PathVariable Long userId,
            @RequestParam(defaultValue = "30") int days) {
        
        LocalDateTime endDate = LocalDateTime.now();
        LocalDateTime startDate = endDate.minusDays(days);
        
        // Statistiques des repas
        var meals = mealService.getUserMeals(userId, startDate, endDate);
        
        // Statistiques des symptômes
        var symptoms = symptomService.getUserSymptoms(userId, startDate, endDate);
        
        // Analyse des allergies
        var potentialAllergies = allergyDetectionService.detectPotentialAllergies(userId, 30.0);
        var highRiskAllergies = potentialAllergies.stream()
                .filter(allergy -> allergy.getRiskScore() >= 50)
                .toList();
        
        // Plans hebdomadaires
        var currentWeekPlans = weeklyPlanService.getCurrentWeekPlan(userId);
        
        // Événements buffet créés
        var userBuffetEvents = buffetService.getUserBuffetEvents(userId);
        
        // Calculs de fréquence des aliments
        Map<String, Integer> foodFrequency = new HashMap<>();
        meals.forEach(meal -> {
            String foodName = meal.getFoodName();
            foodFrequency.put(foodName, foodFrequency.getOrDefault(foodName, 0) + 1);
        });
        
        var mostConsumedFoods = foodFrequency.entrySet().stream()
                .sorted(Map.Entry.<String, Integer>comparingByValue().reversed())
                .limit(5)
                .map(entry -> Map.of("food", entry.getKey(), "count", entry.getValue()))
                .toList();
        
        // Calculs de fréquence des symptômes
        Map<String, Integer> symptomFrequency = new HashMap<>();
        symptoms.forEach(symptom -> {
            String symptomType = symptom.getSymptomType();
            symptomFrequency.put(symptomType, symptomFrequency.getOrDefault(symptomType, 0) + 1);
        });
        
        Map<String, Object> response = new HashMap<>();
        response.put("user_id", userId);
        response.put("period_days", days);
        response.put("generated_at", LocalDateTime.now());
        
        // Statistiques générales
        Map<String, Object> statistics = new HashMap<>();
        statistics.put("total_meals", meals.size());
        statistics.put("total_symptoms", symptoms.size());
        statistics.put("avg_meals_per_day", Math.round((double) meals.size() / days * 10.0) / 10.0);
        statistics.put("avg_symptoms_per_day", Math.round((double) symptoms.size() / days * 10.0) / 10.0);
        statistics.put("unique_foods_consumed", foodFrequency.size());
        statistics.put("unique_symptom_types", symptomFrequency.size());
        response.put("statistics", statistics);
        
        // Analyse des allergies
        Map<String, Object> allergyAnalysis = new HashMap<>();
        allergyAnalysis.put("total_potential_allergies", potentialAllergies.size());
        allergyAnalysis.put("high_risk_foods", highRiskAllergies.size());
        allergyAnalysis.put("top_risks", potentialAllergies.stream().limit(3).toList());
        response.put("allergy_analysis", allergyAnalysis);
        
        // Patterns de consommation
        Map<String, Object> consumptionPatterns = new HashMap<>();
        consumptionPatterns.put("most_consumed_foods", mostConsumedFoods);
        consumptionPatterns.put("symptom_frequency", symptomFrequency);
        response.put("consumption_patterns", consumptionPatterns);
        
        // Planification
        Map<String, Object> planning = new HashMap<>();
        planning.put("current_week_plans", currentWeekPlans.size());
        planning.put("buffet_events_created", userBuffetEvents.size());
        response.put("planning", planning);
        
        // Recommandations
        response.put("recommendations", generateRecommendations(
                meals.size(), symptoms.size(), potentialAllergies.size(), 
                highRiskAllergies.size(), currentWeekPlans.size()));
        
        return ResponseEntity.ok(response);
    }

    @GetMapping("/admin/global-statistics")
    @Operation(summary = "Get global platform statistics")
    public ResponseEntity<Map<String, Object>> getGlobalStatistics() {
        // Cette endpoint pourrait être protégée par des rôles d'administration
        
        Map<String, Object> response = new HashMap<>();
        response.put("message", "Statistiques globales de la plateforme");
        response.put("note", "Cette fonctionnalité nécessite l'implémentation d'un système d'authentification");
        
        return ResponseEntity.ok(response);
    }

    private Map<String, Object> generateRecommendations(int mealsCount, int symptomsCount, 
                                                       int allergiesCount, int highRiskCount, 
                                                       int plansCount) {
        Map<String, Object> recommendations = new HashMap<>();
        
        if (highRiskCount > 0) {
            recommendations.put("priority", "HIGH");
            recommendations.put("message", "Vous avez " + highRiskCount + " aliment(s) à risque élevé. Consultez un médecin.");
        } else if (allergiesCount > 0) {
            recommendations.put("priority", "MEDIUM");
            recommendations.put("message", "Surveillez votre consommation de " + allergiesCount + " aliment(s) potentiellement problématique(s).");
        } else {
            recommendations.put("priority", "LOW");
            recommendations.put("message", "Aucune allergie majeure détectée. Continuez à surveiller votre alimentation.");
        }
        
        if (plansCount == 0) {
            recommendations.put("planning_suggestion", "Créez un plan hebdomadaire pour mieux organiser vos repas.");
        }
        
        if (mealsCount < 21) { // Moins de 3 repas par jour en moyenne
            recommendations.put("meal_frequency", "Essayez de maintenir 3 repas réguliers par jour.");
        }
        
        return recommendations;
    }
}
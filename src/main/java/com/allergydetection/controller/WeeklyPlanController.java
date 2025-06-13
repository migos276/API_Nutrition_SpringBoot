package com.allergydetection.controller;

import com.allergydetection.dto.WeeklyPlanDto;
import com.allergydetection.service.WeeklyPlanService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api")
@Tag(name = "Weekly Planning", description = "APIs for weekly meal planning")
public class WeeklyPlanController {

    @Autowired
    private WeeklyPlanService weeklyPlanService;

    @PostMapping("/weekly-plans")
    @Operation(summary = "Create a weekly plan item")
    public ResponseEntity<Map<String, Object>> createWeeklyPlanItem(@Valid @RequestBody WeeklyPlanDto planDto) {
        WeeklyPlanDto createdPlan = weeklyPlanService.createWeeklyPlanItem(planDto);
        
        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        response.put("plan_id", createdPlan.getId());
        response.put("message", "Élément du plan hebdomadaire créé avec succès");
        response.put("plan", createdPlan);
        
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping("/users/{userId}/weekly-plans")
    @Operation(summary = "Get user weekly plans")
    public ResponseEntity<Map<String, Object>> getUserWeeklyPlans(
            @PathVariable Long userId,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate week_start_date) {
        
        List<WeeklyPlanDto> plans = weeklyPlanService.getUserWeeklyPlan(userId, week_start_date);
        
        Map<String, Object> response = new HashMap<>();
        response.put("user_id", userId);
        response.put("week_start_date", week_start_date);
        response.put("weekly_plans", plans);
        response.put("total_items", plans.size());
        
        // Organiser par jour et type de repas pour faciliter l'affichage
        Map<String, Map<String, WeeklyPlanDto>> organizedPlans = new HashMap<>();
        for (WeeklyPlanDto plan : plans) {
            String dayName = plan.getDayName();
            String mealType = plan.getMealType();
            
            organizedPlans.computeIfAbsent(dayName, k -> new HashMap<>()).put(mealType, plan);
        }
        response.put("organized_plans", organizedPlans);
        
        return ResponseEntity.ok(response);
    }

    @GetMapping("/users/{userId}/weekly-plans/current")
    @Operation(summary = "Get current week plan for user")
    public ResponseEntity<Map<String, Object>> getCurrentWeekPlan(@PathVariable Long userId) {
        List<WeeklyPlanDto> plans = weeklyPlanService.getCurrentWeekPlan(userId);
        
        Map<String, Object> response = new HashMap<>();
        response.put("user_id", userId);
        response.put("current_week_plans", plans);
        response.put("total_items", plans.size());
        
        return ResponseEntity.ok(response);
    }

    @PutMapping("/weekly-plans/{id}")
    @Operation(summary = "Update weekly plan item")
    public ResponseEntity<Map<String, Object>> updateWeeklyPlanItem(
            @PathVariable Long id, 
            @Valid @RequestBody WeeklyPlanDto planDto) {
        
        WeeklyPlanDto updatedPlan = weeklyPlanService.updateWeeklyPlanItem(id, planDto);
        
        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        response.put("message", "Plan hebdomadaire mis à jour avec succès");
        response.put("plan", updatedPlan);
        
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/weekly-plans/{id}")
    @Operation(summary = "Delete weekly plan item")
    public ResponseEntity<Map<String, Object>> deleteWeeklyPlanItem(@PathVariable Long id) {
        weeklyPlanService.deleteWeeklyPlanItem(id);
        
        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        response.put("message", "Élément du plan hebdomadaire supprimé avec succès");
        
        return ResponseEntity.ok(response);
    }

    @PostMapping("/users/{userId}/weekly-plans/duplicate")
    @Operation(summary = "Duplicate a week plan to another week")
    public ResponseEntity<Map<String, Object>> duplicateWeekPlan(
            @PathVariable Long userId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate source_week,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate target_week) {
        
        weeklyPlanService.duplicateWeekPlan(userId, source_week, target_week);
        
        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        response.put("message", "Plan hebdomadaire dupliqué avec succès");
        response.put("source_week", source_week);
        response.put("target_week", target_week);
        
        return ResponseEntity.ok(response);
    }

    @GetMapping("/users/{userId}/weekly-plans/suggestions")
    @Operation(summary = "Get weekly plan suggestions based on user history")
    public ResponseEntity<Map<String, Object>> getWeeklyPlanSuggestions(@PathVariable Long userId) {
        List<WeeklyPlanDto> suggestions = weeklyPlanService.generateWeeklyPlanSuggestions(userId);
        
        Map<String, Object> response = new HashMap<>();
        response.put("user_id", userId);
        response.put("suggestions", suggestions);
        response.put("total_suggestions", suggestions.size());
        response.put("message", "Suggestions basées sur votre historique alimentaire et vos allergies détectées");
        
        return ResponseEntity.ok(response);
    }

    @PostMapping("/users/{userId}/weekly-plans/batch")
    @Operation(summary = "Create multiple weekly plan items at once")
    public ResponseEntity<Map<String, Object>> createBatchWeeklyPlan(
            @PathVariable Long userId,
            @RequestBody Map<String, Object> batchData) {
        
        @SuppressWarnings("unchecked")
        List<Map<String, Object>> planItems = (List<Map<String, Object>>) batchData.get("plans");
        LocalDate weekStartDate = LocalDate.parse((String) batchData.get("week_start_date"));
        
        int createdCount = 0;
        for (Map<String, Object> item : planItems) {
            WeeklyPlanDto planDto = new WeeklyPlanDto();
            planDto.setUserId(userId);
            planDto.setWeekStartDate(weekStartDate);
            planDto.setDayOfWeek((Integer) item.get("day_of_week"));
            planDto.setMealType((String) item.get("meal_type"));
            planDto.setFoodId(Long.valueOf(item.get("food_id").toString()));
            planDto.setPlannedQuantity(Double.valueOf(item.get("planned_quantity").toString()));
            
            weeklyPlanService.createWeeklyPlanItem(planDto);
            createdCount++;
        }
        
        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        response.put("message", "Plan hebdomadaire créé avec succès");
        response.put("items_created", createdCount);
        response.put("week_start_date", weekStartDate);
        
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }
}
package com.allergydetection.service;

import com.allergydetection.dto.WeeklyPlanDto;
import com.allergydetection.entity.Food;
import com.allergydetection.entity.User;
import com.allergydetection.entity.WeeklyPlan;
import com.allergydetection.exception.ResourceNotFoundException;
import com.allergydetection.repository.FoodRepository;
import com.allergydetection.repository.UserRepository;
import com.allergydetection.repository.WeeklyPlanRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.temporal.WeekFields;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

@Service
@Transactional
public class WeeklyPlanService {

    @Autowired
    private WeeklyPlanRepository weeklyPlanRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private FoodRepository foodRepository;

    public WeeklyPlanDto createWeeklyPlanItem(WeeklyPlanDto planDto) {
        User user = userRepository.findById(planDto.getUserId())
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + planDto.getUserId()));

        Food food = foodRepository.findById(planDto.getFoodId())
                .orElseThrow(() -> new ResourceNotFoundException("Food not found with id: " + planDto.getFoodId()));

        WeeklyPlan plan = new WeeklyPlan(
                user,
                planDto.getWeekStartDate(),
                planDto.getDayOfWeek(),
                planDto.getMealType(),
                food,
                planDto.getPlannedQuantity()
        );

        WeeklyPlan savedPlan = weeklyPlanRepository.save(plan);
        return convertToDto(savedPlan);
    }

    @Transactional(readOnly = true)
    public List<WeeklyPlanDto> getUserWeeklyPlan(Long userId, LocalDate weekStartDate) {
        if (!userRepository.existsById(userId)) {
            throw new ResourceNotFoundException("User not found with id: " + userId);
        }

        List<WeeklyPlan> plans;
        if (weekStartDate != null) {
            plans = weeklyPlanRepository.findByUserIdAndWeekStartDate(userId, weekStartDate);
        } else {
            plans = weeklyPlanRepository.findByUserIdOrderByDayOfWeekAscMealType(userId);
        }

        return plans.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<WeeklyPlanDto> getCurrentWeekPlan(Long userId) {
        LocalDate now = LocalDate.now();
        LocalDate weekStart = now.with(WeekFields.of(Locale.getDefault()).dayOfWeek(), 1);
        return getUserWeeklyPlan(userId, weekStart);
    }

    public WeeklyPlanDto updateWeeklyPlanItem(Long id, WeeklyPlanDto planDto) {
        WeeklyPlan plan = weeklyPlanRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Weekly plan item not found with id: " + id));

        if (planDto.getFoodId() != null && !planDto.getFoodId().equals(plan.getFood().getId())) {
            Food food = foodRepository.findById(planDto.getFoodId())
                    .orElseThrow(() -> new ResourceNotFoundException("Food not found with id: " + planDto.getFoodId()));
            plan.setFood(food);
        }

        if (planDto.getWeekStartDate() != null) {
            plan.setWeekStartDate(planDto.getWeekStartDate());
        }
        if (planDto.getDayOfWeek() != null) {
            plan.setDayOfWeek(planDto.getDayOfWeek());
        }
        if (planDto.getMealType() != null) {
            plan.setMealType(planDto.getMealType());
        }
        if (planDto.getPlannedQuantity() != null) {
            plan.setPlannedQuantity(planDto.getPlannedQuantity());
        }

        WeeklyPlan updatedPlan = weeklyPlanRepository.save(plan);
        return convertToDto(updatedPlan);
    }

    public void deleteWeeklyPlanItem(Long id) {
        if (!weeklyPlanRepository.existsById(id)) {
            throw new ResourceNotFoundException("Weekly plan item not found with id: " + id);
        }
        weeklyPlanRepository.deleteById(id);
    }

    public List<WeeklyPlanDto> generateWeeklyPlanSuggestions(Long userId) {
        // Logique pour générer des suggestions basées sur l'historique de l'utilisateur
        // et les allergies détectées
        if (!userRepository.existsById(userId)) {
            throw new ResourceNotFoundException("User not found with id: " + userId);
        }

        // Pour l'instant, retourner une liste vide
        // Cette méthode peut être étendue avec une logique d'IA plus sophistiquée
        return List.of();
    }

    public WeeklyPlanDto duplicateWeekPlan(Long userId, LocalDate sourceWeek, LocalDate targetWeek) {
        List<WeeklyPlan> sourcePlans = weeklyPlanRepository.findByUserIdAndWeekStartDate(userId, sourceWeek);
        
        if (sourcePlans.isEmpty()) {
            throw new ResourceNotFoundException("No weekly plan found for the specified week");
        }

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + userId));

        for (WeeklyPlan sourcePlan : sourcePlans) {
            WeeklyPlan newPlan = new WeeklyPlan(
                    user,
                    targetWeek,
                    sourcePlan.getDayOfWeek(),
                    sourcePlan.getMealType(),
                    sourcePlan.getFood(),
                    sourcePlan.getPlannedQuantity()
            );
            weeklyPlanRepository.save(newPlan);
        }

        return new WeeklyPlanDto(); // Retourner un DTO de confirmation
    }

    private WeeklyPlanDto convertToDto(WeeklyPlan plan) {
        return new WeeklyPlanDto(
                plan.getId(),
                plan.getUser().getId(),
                plan.getWeekStartDate(),
                plan.getDayOfWeek(),
                plan.getMealType(),
                plan.getFood().getId(),
                plan.getPlannedQuantity(),
                plan.getFood().getName(),
                plan.getFood().getCategory(),
                plan.getFood().getIngredients()
        );
    }
}
package com.allergydetection.service;

import com.allergydetection.dto.MealDto;
import com.allergydetection.entity.Food;
import com.allergydetection.entity.Meal;
import com.allergydetection.entity.User;
import com.allergydetection.exception.ResourceNotFoundException;
import com.allergydetection.repository.FoodRepository;
import com.allergydetection.repository.MealRepository;
import com.allergydetection.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class MealService {

    @Autowired
    private MealRepository mealRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private FoodRepository foodRepository;

    public MealDto createMeal(MealDto mealDto) {
        User user = userRepository.findById(mealDto.getUserId())
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + mealDto.getUserId()));

        Food food = foodRepository.findById(mealDto.getFoodId())
                .orElseThrow(() -> new ResourceNotFoundException("Food not found with id: " + mealDto.getFoodId()));

        Meal meal = new Meal(user, food, mealDto.getMealTime(), mealDto.getQuantity(), mealDto.getNotes());
        Meal savedMeal = mealRepository.save(meal);

        return convertToDto(savedMeal);
    }

    @Transactional(readOnly = true)
    public List<MealDto> getUserMeals(Long userId) {
        if (!userRepository.existsById(userId)) {
            throw new ResourceNotFoundException("User not found with id: " + userId);
        }

        return mealRepository.findByUserIdOrderByMealTimeDesc(userId).stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<MealDto> getUserMeals(Long userId, LocalDateTime startDate, LocalDateTime endDate) {
        if (!userRepository.existsById(userId)) {
            throw new ResourceNotFoundException("User not found with id: " + userId);
        }

        return mealRepository.findByUserIdAndMealTimeBetween(userId, startDate, endDate).stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<MealDto> getUserMealsByFood(Long userId, Long foodId, LocalDateTime startDate, LocalDateTime endDate) {
        if (!userRepository.existsById(userId)) {
            throw new ResourceNotFoundException("User not found with id: " + userId);
        }
        
        if (!foodRepository.existsById(foodId)) {
            throw new ResourceNotFoundException("Food not found with id: " + foodId);
        }

        return mealRepository.findByUserIdAndFoodIdAndMealTimeBetween(userId, foodId, startDate, endDate).stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    public MealDto updateMeal(Long id, MealDto mealDto) {
        Meal meal = mealRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Meal not found with id: " + id));

        if (mealDto.getFoodId() != null && !mealDto.getFoodId().equals(meal.getFood().getId())) {
            Food food = foodRepository.findById(mealDto.getFoodId())
                    .orElseThrow(() -> new ResourceNotFoundException("Food not found with id: " + mealDto.getFoodId()));
            meal.setFood(food);
        }

        if (mealDto.getMealTime() != null) {
            meal.setMealTime(mealDto.getMealTime());
        }
        if (mealDto.getQuantity() != null) {
            meal.setQuantity(mealDto.getQuantity());
        }
        if (mealDto.getNotes() != null) {
            meal.setNotes(mealDto.getNotes());
        }

        Meal updatedMeal = mealRepository.save(meal);
        return convertToDto(updatedMeal);
    }

    public void deleteMeal(Long id) {
        if (!mealRepository.existsById(id)) {
            throw new ResourceNotFoundException("Meal not found with id: " + id);
        }
        mealRepository.deleteById(id);
    }

    private MealDto convertToDto(Meal meal) {
        return new MealDto(
                meal.getId(),
                meal.getUser().getId(),
                meal.getFood().getId(),
                meal.getMealTime(),
                meal.getQuantity(),
                meal.getNotes(),
                meal.getFood().getName(),
                meal.getFood().getIngredients()
        );
    }
}
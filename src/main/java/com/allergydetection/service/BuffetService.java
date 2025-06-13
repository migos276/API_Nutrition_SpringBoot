package com.allergydetection.service;

import com.allergydetection.dto.BuffetEventDto;
import com.allergydetection.dto.BuffetFoodDto;
import com.allergydetection.entity.BuffetEvent;
import com.allergydetection.entity.BuffetFood;
import com.allergydetection.entity.Food;
import com.allergydetection.entity.User;
import com.allergydetection.exception.ResourceNotFoundException;
import com.allergydetection.repository.BuffetEventRepository;
import com.allergydetection.repository.BuffetFoodRepository;
import com.allergydetection.repository.FoodRepository;
import com.allergydetection.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@Transactional
public class BuffetService {

    @Autowired
    private BuffetEventRepository buffetEventRepository;

    @Autowired
    private BuffetFoodRepository buffetFoodRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private FoodRepository foodRepository;

    public BuffetEventDto createBuffetEvent(BuffetEventDto eventDto) {
        User creator = userRepository.findById(eventDto.getCreatedBy())
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + eventDto.getCreatedBy()));

        BuffetEvent event = new BuffetEvent(
                eventDto.getEventName(),
                eventDto.getEventDate(),
                eventDto.getEstimatedGuests(),
                creator
        );

        BuffetEvent savedEvent = buffetEventRepository.save(event);

        // Ajouter les aliments si fournis
        if (eventDto.getFoods() != null && !eventDto.getFoods().isEmpty()) {
            for (BuffetFoodDto foodDto : eventDto.getFoods()) {
                addFoodToBuffet(savedEvent.getId(), foodDto);
            }
        }

        return convertToDto(savedEvent);
    }

    @Transactional(readOnly = true)
    public BuffetEventDto getBuffetEvent(Long eventId) {
        BuffetEvent event = buffetEventRepository.findById(eventId)
                .orElseThrow(() -> new ResourceNotFoundException("Buffet event not found with id: " + eventId));

        BuffetEventDto dto = convertToDto(event);
        
        // Récupérer les aliments du buffet
        List<BuffetFood> buffetFoods = buffetFoodRepository.findByBuffetEventId(eventId);
        List<BuffetFoodDto> foodDtos = buffetFoods.stream()
                .map(this::convertFoodToDto)
                .collect(Collectors.toList());
        
        dto.setFoods(foodDtos);
        return dto;
    }

    @Transactional(readOnly = true)
    public List<BuffetEventDto> getAllBuffetEvents() {
        return buffetEventRepository.findAllByOrderByEventDateDesc().stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<BuffetEventDto> getUserBuffetEvents(Long userId) {
        if (!userRepository.existsById(userId)) {
            throw new ResourceNotFoundException("User not found with id: " + userId);
        }

        return buffetEventRepository.findByCreatedByIdOrderByEventDateDesc(userId).stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    public BuffetEventDto updateBuffetEvent(Long eventId, BuffetEventDto eventDto) {
        BuffetEvent event = buffetEventRepository.findById(eventId)
                .orElseThrow(() -> new ResourceNotFoundException("Buffet event not found with id: " + eventId));

        if (eventDto.getEventName() != null) {
            event.setEventName(eventDto.getEventName());
        }
        if (eventDto.getEventDate() != null) {
            event.setEventDate(eventDto.getEventDate());
        }
        if (eventDto.getEstimatedGuests() != null) {
            event.setEstimatedGuests(eventDto.getEstimatedGuests());
        }

        BuffetEvent updatedEvent = buffetEventRepository.save(event);
        return convertToDto(updatedEvent);
    }

    public void deleteBuffetEvent(Long eventId) {
        if (!buffetEventRepository.existsById(eventId)) {
            throw new ResourceNotFoundException("Buffet event not found with id: " + eventId);
        }
        buffetEventRepository.deleteById(eventId);
    }

    public BuffetFoodDto addFoodToBuffet(Long buffetId, BuffetFoodDto foodDto) {
        BuffetEvent buffetEvent = buffetEventRepository.findById(buffetId)
                .orElseThrow(() -> new ResourceNotFoundException("Buffet event not found with id: " + buffetId));

        Food food = foodRepository.findById(foodDto.getFoodId())
                .orElseThrow(() -> new ResourceNotFoundException("Food not found with id: " + foodDto.getFoodId()));

        BuffetFood buffetFood = new BuffetFood(
                buffetEvent,
                food,
                foodDto.getPlannedQuantity(),
                foodDto.getUnit() != null ? foodDto.getUnit() : "portions"
        );

        BuffetFood savedBuffetFood = buffetFoodRepository.save(buffetFood);
        return convertFoodToDto(savedBuffetFood);
    }

    public void removeFoodFromBuffet(Long buffetFoodId) {
        if (!buffetFoodRepository.existsById(buffetFoodId)) {
            throw new ResourceNotFoundException("Buffet food not found with id: " + buffetFoodId);
        }
        buffetFoodRepository.deleteById(buffetFoodId);
    }

    public Map<String, Object> calculateBuffetQuantities(Long buffetId) {
        BuffetEvent event = buffetEventRepository.findById(buffetId)
                .orElseThrow(() -> new ResourceNotFoundException("Buffet event not found with id: " + buffetId));

        List<BuffetFood> buffetFoods = buffetFoodRepository.findByBuffetEventId(buffetId);
        Integer estimatedGuests = event.getEstimatedGuests();

        List<Map<String, Object>> recommendations = buffetFoods.stream()
                .map(buffetFood -> {
                    Map<String, Object> recommendation = new HashMap<>();
                    
                    // Calculs basiques selon la catégorie
                    String category = buffetFood.getFood().getCategory();
                    double recommendedPerPerson;
                    
                    switch (category != null ? category : "") {
                        case "Plat principal":
                            recommendedPerPerson = 1.2; // 1.2 portions par personne
                            break;
                        case "Accompagnement":
                        case "Légume":
                            recommendedPerPerson = 0.8;
                            break;
                        case "Dessert":
                        case "Boisson":
                            recommendedPerPerson = 1.0;
                            break;
                        default:
                            recommendedPerPerson = 1.0;
                    }
                    
                    double totalRecommended = estimatedGuests * recommendedPerPerson;
                    
                    recommendation.put("food_id", buffetFood.getFood().getId());
                    recommendation.put("food_name", buffetFood.getFood().getName());
                    recommendation.put("category", category);
                    recommendation.put("planned_quantity", buffetFood.getPlannedQuantity());
                    recommendation.put("recommended_quantity", Math.round(totalRecommended * 10.0) / 10.0);
                    recommendation.put("per_person", recommendedPerPerson);
                    recommendation.put("unit", buffetFood.getUnit());
                    recommendation.put("difference", Math.round((totalRecommended - buffetFood.getPlannedQuantity()) * 10.0) / 10.0);
                    
                    return recommendation;
                })
                .collect(Collectors.toList());

        Map<String, Object> result = new HashMap<>();
        result.put("buffet_id", buffetId);
        result.put("event_name", event.getEventName());
        result.put("estimated_guests", estimatedGuests);
        result.put("recommendations", recommendations);
        result.put("total_foods", recommendations.size());

        return result;
    }

    public Map<String, Object> getBuffetShoppingList(Long buffetId) {
        BuffetEvent event = buffetEventRepository.findById(buffetId)
                .orElseThrow(() -> new ResourceNotFoundException("Buffet event not found with id: " + buffetId));

        List<BuffetFood> buffetFoods = buffetFoodRepository.findByBuffetEventId(buffetId);

        // Grouper par catégorie pour faciliter les achats
        Map<String, List<Map<String, Object>>> shoppingByCategory = buffetFoods.stream()
                .collect(Collectors.groupingBy(
                        bf -> bf.getFood().getCategory() != null ? bf.getFood().getCategory() : "Autres",
                        Collectors.mapping(bf -> {
                            Map<String, Object> item = new HashMap<>();
                            item.put("food_name", bf.getFood().getName());
                            item.put("quantity", bf.getPlannedQuantity());
                            item.put("unit", bf.getUnit());
                            item.put("ingredients", bf.getFood().getIngredients());
                            return item;
                        }, Collectors.toList())
                ));

        Map<String, Object> result = new HashMap<>();
        result.put("buffet_id", buffetId);
        result.put("event_name", event.getEventName());
        result.put("event_date", event.getEventDate());
        result.put("estimated_guests", event.getEstimatedGuests());
        result.put("shopping_list", shoppingByCategory);
        result.put("total_items", buffetFoods.size());

        return result;
    }

    private BuffetEventDto convertToDto(BuffetEvent event) {
        return new BuffetEventDto(
                event.getId(),
                event.getEventName(),
                event.getEventDate(),
                event.getEstimatedGuests(),
                event.getCreatedBy().getId(),
                event.getCreatedBy().getUsername()
        );
    }

    private BuffetFoodDto convertFoodToDto(BuffetFood buffetFood) {
        return new BuffetFoodDto(
                buffetFood.getId(),
                buffetFood.getBuffetEvent().getId(),
                buffetFood.getFood().getId(),
                buffetFood.getPlannedQuantity(),
                buffetFood.getUnit(),
                buffetFood.getFood().getName(),
                buffetFood.getFood().getCategory(),
                buffetFood.getFood().getIngredients()
        );
    }
}
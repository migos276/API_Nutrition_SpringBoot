package com.allergydetection.controller;

import com.allergydetection.dto.BuffetEventDto;
import com.allergydetection.dto.BuffetFoodDto;
import com.allergydetection.service.BuffetService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api")
@Tag(name = "Buffet Management", description = "APIs for managing buffet events")
public class BuffetController {

    @Autowired
    private BuffetService buffetService;

    @PostMapping("/buffet-events")
    @Operation(summary = "Create a new buffet event")
    public ResponseEntity<Map<String, Object>> createBuffetEvent(@Valid @RequestBody BuffetEventDto eventDto) {
        BuffetEventDto createdEvent = buffetService.createBuffetEvent(eventDto);
        
        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        response.put("buffet_id", createdEvent.getId());
        response.put("message", "Événement buffet créé avec succès");
        response.put("event", createdEvent);
        
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping("/buffet-events/{eventId}")
    @Operation(summary = "Get buffet event details")
    public ResponseEntity<BuffetEventDto> getBuffetEvent(@PathVariable Long eventId) {
        BuffetEventDto event = buffetService.getBuffetEvent(eventId);
        return ResponseEntity.ok(event);
    }

    @GetMapping("/buffet-events")
    @Operation(summary = "Get all buffet events")
    public ResponseEntity<Map<String, Object>> getAllBuffetEvents() {
        List<BuffetEventDto> events = buffetService.getAllBuffetEvents();
        
        Map<String, Object> response = new HashMap<>();
        response.put("buffet_events", events);
        response.put("total_events", events.size());
        
        return ResponseEntity.ok(response);
    }

    @GetMapping("/users/{userId}/buffet-events")
    @Operation(summary = "Get user's buffet events")
    public ResponseEntity<Map<String, Object>> getUserBuffetEvents(@PathVariable Long userId) {
        List<BuffetEventDto> events = buffetService.getUserBuffetEvents(userId);
        
        Map<String, Object> response = new HashMap<>();
        response.put("user_id", userId);
        response.put("buffet_events", events);
        response.put("total_events", events.size());
        
        return ResponseEntity.ok(response);
    }

    @PutMapping("/buffet-events/{eventId}")
    @Operation(summary = "Update buffet event")
    public ResponseEntity<Map<String, Object>> updateBuffetEvent(
            @PathVariable Long eventId, 
            @Valid @RequestBody BuffetEventDto eventDto) {
        
        BuffetEventDto updatedEvent = buffetService.updateBuffetEvent(eventId, eventDto);
        
        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        response.put("message", "Événement buffet mis à jour avec succès");
        response.put("event", updatedEvent);
        
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/buffet-events/{eventId}")
    @Operation(summary = "Delete buffet event")
    public ResponseEntity<Map<String, Object>> deleteBuffetEvent(@PathVariable Long eventId) {
        buffetService.deleteBuffetEvent(eventId);
        
        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        response.put("message", "Événement buffet supprimé avec succès");
        
        return ResponseEntity.ok(response);
    }

    @PostMapping("/buffet-events/{buffetId}/foods")
    @Operation(summary = "Add food to buffet")
    public ResponseEntity<Map<String, Object>> addFoodToBuffet(
            @PathVariable Long buffetId, 
            @Valid @RequestBody BuffetFoodDto foodDto) {
        
        foodDto.setBuffetId(buffetId);
        BuffetFoodDto addedFood = buffetService.addFoodToBuffet(buffetId, foodDto);
        
        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        response.put("message", "Aliment ajouté au buffet avec succès");
        response.put("buffet_food", addedFood);
        
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @DeleteMapping("/buffet-foods/{buffetFoodId}")
    @Operation(summary = "Remove food from buffet")
    public ResponseEntity<Map<String, Object>> removeFoodFromBuffet(@PathVariable Long buffetFoodId) {
        buffetService.removeFoodFromBuffet(buffetFoodId);
        
        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        response.put("message", "Aliment retiré du buffet avec succès");
        
        return ResponseEntity.ok(response);
    }

    @GetMapping("/buffet-events/{buffetId}/calculate-quantities")
    @Operation(summary = "Calculate recommended quantities for buffet")
    public ResponseEntity<Map<String, Object>> calculateBuffetQuantities(@PathVariable Long buffetId) {
        Map<String, Object> calculations = buffetService.calculateBuffetQuantities(buffetId);
        return ResponseEntity.ok(calculations);
    }

    @GetMapping("/buffet-events/{buffetId}/shopping-list")
    @Operation(summary = "Generate shopping list for buffet")
    public ResponseEntity<Map<String, Object>> getBuffetShoppingList(@PathVariable Long buffetId) {
        Map<String, Object> shoppingList = buffetService.getBuffetShoppingList(buffetId);
        return ResponseEntity.ok(shoppingList);
    }

    @PostMapping("/buffet-events/{buffetId}/optimize")
    @Operation(summary = "Optimize buffet menu based on guest allergies")
    public ResponseEntity<Map<String, Object>> optimizeBuffetMenu(
            @PathVariable Long buffetId,
            @RequestBody Map<String, Object> optimizationData) {
        
        // Cette fonctionnalité peut être étendue pour analyser les allergies des invités
        // et suggérer des modifications au menu
        
        @SuppressWarnings("unchecked")
        List<Long> guestUserIds = (List<Long>) optimizationData.get("guest_user_ids");
        
        Map<String, Object> response = new HashMap<>();
        response.put("buffet_id", buffetId);
        response.put("analyzed_guests", guestUserIds != null ? guestUserIds.size() : 0);
        response.put("message", "Analyse d'optimisation du buffet en cours de développement");
        response.put("recommendations", List.of(
            "Vérifiez les allergies communes parmi les invités",
            "Assurez-vous d'avoir des alternatives sans allergènes",
            "Étiquetez clairement tous les plats avec leurs ingrédients"
        ));
        
        return ResponseEntity.ok(response);
    }

    @GetMapping("/buffet-events/{buffetId}/cost-estimation")
    @Operation(summary = "Estimate cost for buffet event")
    public ResponseEntity<Map<String, Object>> estimateBuffetCost(
            @PathVariable Long buffetId,
            @RequestParam(defaultValue = "1000") Double budget_per_person) {
        
        BuffetEventDto event = buffetService.getBuffetEvent(buffetId);
        
        // Calcul basique du coût estimé
        Double totalBudget = event.getEstimatedGuests() * budget_per_person;
        Double costPerFood = event.getFoods() != null && !event.getFoods().isEmpty() 
            ? totalBudget / event.getFoods().size() 
            : 0.0;
        
        Map<String, Object> response = new HashMap<>();
        response.put("buffet_id", buffetId);
        response.put("estimated_guests", event.getEstimatedGuests());
        response.put("budget_per_person", budget_per_person);
        response.put("total_estimated_budget", totalBudget);
        response.put("cost_per_food_item", Math.round(costPerFood * 100.0) / 100.0);
        response.put("total_food_items", event.getFoods() != null ? event.getFoods().size() : 0);
        response.put("currency", "FCFA");
        
        return ResponseEntity.ok(response);
    }
}
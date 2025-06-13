package com.allergydetection.controller;

import com.allergydetection.dto.SymptomDto;
import com.allergydetection.service.SymptomService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api")
@Tag(name = "Symptom Management", description = "APIs for managing symptoms")
public class SymptomController {

    @Autowired
    private SymptomService symptomService;

    @PostMapping("/symptoms")
    @Operation(summary = "Create a new symptom")
    public ResponseEntity<Map<String, Object>> createSymptom(@Valid @RequestBody SymptomDto symptomDto) {
        SymptomDto createdSymptom = symptomService.createSymptom(symptomDto);
        
        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        response.put("symptom_id", createdSymptom.getId());
        response.put("message", "Symptôme enregistré avec succès");
        
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping("/users/{userId}/symptoms")
    @Operation(summary = "Get user symptoms with optional date filtering")
    public ResponseEntity<Map<String, Object>> getUserSymptoms(
            @PathVariable Long userId,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime start_date,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime end_date) {
        
        List<SymptomDto> symptoms;
        if (start_date != null && end_date != null) {
            symptoms = symptomService.getUserSymptoms(userId, start_date, end_date);
        } else {
            symptoms = symptomService.getUserSymptoms(userId);
        }
        
        Map<String, Object> response = new HashMap<>();
        response.put("symptoms", symptoms);
        
        return ResponseEntity.ok(response);
    }

    @PutMapping("/symptoms/{id}")
    @Operation(summary = "Update symptom")
    public ResponseEntity<Map<String, Object>> updateSymptom(@PathVariable Long id, @Valid @RequestBody SymptomDto symptomDto) {
        SymptomDto updatedSymptom = symptomService.updateSymptom(id, symptomDto);
        
        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        response.put("message", "Symptôme mis à jour avec succès");
        response.put("symptom", updatedSymptom);
        
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/symptoms/{id}")
    @Operation(summary = "Delete symptom")
    public ResponseEntity<Map<String, Object>> deleteSymptom(@PathVariable Long id) {
        symptomService.deleteSymptom(id);
        
        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        response.put("message", "Symptôme supprimé avec succès");
        
        return ResponseEntity.ok(response);
    }
}
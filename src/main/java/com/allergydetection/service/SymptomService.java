package com.allergydetection.service;

import com.allergydetection.dto.SymptomDto;
import com.allergydetection.entity.Symptom;
import com.allergydetection.entity.User;
import com.allergydetection.exception.ResourceNotFoundException;
import com.allergydetection.repository.SymptomRepository;
import com.allergydetection.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class SymptomService {

    @Autowired
    private SymptomRepository symptomRepository;

    @Autowired
    private UserRepository userRepository;

    public SymptomDto createSymptom(SymptomDto symptomDto) {
        User user = userRepository.findById(symptomDto.getUserId())
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + symptomDto.getUserId()));

        Symptom symptom = new Symptom(
                user,
                symptomDto.getSymptomType(),
                symptomDto.getSeverity(),
                symptomDto.getOccurrenceTime(),
                symptomDto.getDescription()
        );

        Symptom savedSymptom = symptomRepository.save(symptom);
        return convertToDto(savedSymptom);
    }

    @Transactional(readOnly = true)
    public List<SymptomDto> getUserSymptoms(Long userId) {
        if (!userRepository.existsById(userId)) {
            throw new ResourceNotFoundException("User not found with id: " + userId);
        }

        return symptomRepository.findByUserIdOrderByOccurrenceTimeDesc(userId).stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<SymptomDto> getUserSymptoms(Long userId, LocalDateTime startDate, LocalDateTime endDate) {
        if (!userRepository.existsById(userId)) {
            throw new ResourceNotFoundException("User not found with id: " + userId);
        }

        return symptomRepository.findByUserIdAndOccurrenceTimeBetween(userId, startDate, endDate).stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<SymptomDto> getUserSymptomsByType(Long userId, String symptomType) {
        if (!userRepository.existsById(userId)) {
            throw new ResourceNotFoundException("User not found with id: " + userId);
        }

        return symptomRepository.findByUserIdAndSymptomType(userId, symptomType).stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    public SymptomDto updateSymptom(Long id, SymptomDto symptomDto) {
        Symptom symptom = symptomRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Symptom not found with id: " + id));

        if (symptomDto.getSymptomType() != null) {
            symptom.setSymptomType(symptomDto.getSymptomType());
        }
        if (symptomDto.getSeverity() != null) {
            symptom.setSeverity(symptomDto.getSeverity());
        }
        if (symptomDto.getOccurrenceTime() != null) {
            symptom.setOccurrenceTime(symptomDto.getOccurrenceTime());
        }
        if (symptomDto.getDescription() != null) {
            symptom.setDescription(symptomDto.getDescription());
        }

        Symptom updatedSymptom = symptomRepository.save(symptom);
        return convertToDto(updatedSymptom);
    }

    public void deleteSymptom(Long id) {
        if (!symptomRepository.existsById(id)) {
            throw new ResourceNotFoundException("Symptom not found with id: " + id);
        }
        symptomRepository.deleteById(id);
    }

    private SymptomDto convertToDto(Symptom symptom) {
        return new SymptomDto(
                symptom.getId(),
                symptom.getUser().getId(),
                symptom.getSymptomType(),
                symptom.getSeverity(),
                symptom.getOccurrenceTime(),
                symptom.getDescription()
        );
    }
}
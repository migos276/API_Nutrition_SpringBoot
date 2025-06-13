package com.allergydetection.repository;

import com.allergydetection.entity.BuffetEvent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BuffetEventRepository extends JpaRepository<BuffetEvent, Long> {
    List<BuffetEvent> findByCreatedByIdOrderByEventDateDesc(Long createdById);
    List<BuffetEvent> findAllByOrderByEventDateDesc();
}
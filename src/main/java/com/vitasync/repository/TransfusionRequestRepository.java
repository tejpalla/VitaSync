package com.vitasync.repository;

import com.vitasync.entity.TransfusionRequest;
import com.vitasync.enums.BloodType;
import com.vitasync.enums.RequestStatus;
import com.vitasync.enums.Urgency;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface TransfusionRequestRepository extends JpaRepository<TransfusionRequest, Long> {
    
    Page<TransfusionRequest> findByStatusAndUrgencyAndBloodType(
        RequestStatus status, Urgency urgency, BloodType bloodType, Pageable pageable);
    
    Page<TransfusionRequest> findByStatus(RequestStatus status, Pageable pageable);
    
    Page<TransfusionRequest> findByBloodType(BloodType bloodType, Pageable pageable);
    
    List<TransfusionRequest> findByPatientId(Long patientId);
    
    @Query("SELECT tr FROM TransfusionRequest tr WHERE tr.urgency IN ('HIGH', 'CRITICAL') AND tr.status = 'PENDING'")
    List<TransfusionRequest> findUrgentRequests();
    
    @Query("SELECT COUNT(tr) FROM TransfusionRequest tr WHERE tr.status = :status")
    Long countByStatus(@Param("status") RequestStatus status);
    
    @Query("SELECT tr FROM TransfusionRequest tr WHERE tr.requiredByDate < :dateTime AND tr.status = 'PENDING'")
    List<TransfusionRequest> findOverdueRequests(@Param("dateTime") LocalDateTime dateTime);
    
    @Query("SELECT tr FROM TransfusionRequest tr WHERE tr.createdAt >= :since ORDER BY tr.createdAt DESC")
    List<TransfusionRequest> findRecentRequests(@Param("since") LocalDateTime since);
}
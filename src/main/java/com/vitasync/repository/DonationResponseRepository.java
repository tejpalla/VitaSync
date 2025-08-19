package com.vitasync.repository;

import com.vitasync.entity.DonationResponse;
import com.vitasync.enums.DonationStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface DonationResponseRepository extends JpaRepository<DonationResponse, Long> {
    
    List<DonationResponse> findByRequestId(Long requestId);
    
    List<DonationResponse> findByDonorId(Long donorId);
    
    List<DonationResponse> findByRequestIdAndStatus(Long requestId, DonationStatus status);
    
    @Query("SELECT COUNT(dr) FROM DonationResponse dr WHERE dr.request.id = :requestId")
    Integer countResponsesByRequestId(@Param("requestId") Long requestId);
    
    @Query("SELECT COUNT(dr) FROM DonationResponse dr WHERE dr.status = 'COMPLETED'")
    Long countSuccessfulDonations();
}
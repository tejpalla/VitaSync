package com.vitasync.repository;

import com.vitasync.entity.BloodBank;
import com.vitasync.enums.BloodType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface BloodBankRepository extends JpaRepository<BloodBank, Long> {
    
    @Query("SELECT bb FROM BloodBank bb JOIN bb.availableBloodTypes bt WHERE bt = :bloodType")
    List<BloodBank> findByAvailableBloodType(@Param("bloodType") BloodType bloodType);
    
    @Query(value = "SELECT * FROM blood_banks WHERE " +
           "(6371 * acos(cos(radians(:lat)) * cos(radians(latitude)) * " +
           "cos(radians(longitude) - radians(:lng)) + sin(radians(:lat)) * " +
           "sin(radians(latitude)))) < :radius", nativeQuery = true)
    List<BloodBank> findNearbyBloodBanks(
        @Param("lat") Double latitude, 
        @Param("lng") Double longitude, 
        @Param("radius") Double radius);
}
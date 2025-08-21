package com.vitasync.repository;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.vitasync.entity.User;
import com.vitasync.enums.BloodType;
import com.vitasync.enums.UserRole;

public interface UserRepository extends JpaRepository<User, Long> {
    
    Optional<User> findByEmail(String email);
    
    boolean existsByEmail(String email);
    
    // Add these missing methods
    @Query("SELECT COUNT(u) FROM User u WHERE u.role = :role")
    Long countByRole(@Param("role") UserRole role);
    
    @Query("SELECT COUNT(u) FROM User u WHERE u.role = :role AND u.isAvailable = :isAvailable")
    Long countByRoleAndIsAvailable(@Param("role") UserRole role, @Param("isAvailable") Boolean isAvailable);
    
    @Query("SELECT u.bloodType, COUNT(u) FROM User u WHERE u.role = 'DONOR' GROUP BY u.bloodType")
    List<Object[]> countDonorsByBloodType();
    
    @Query("SELECT u FROM User u WHERE u.role = 'DONOR' AND u.isAvailable = true AND u.bloodType IN :bloodTypes")
    List<User> findAvailableDonorsByBloodTypes(@Param("bloodTypes") Set<BloodType> bloodTypes);
    
    Page<User> findByRoleAndBloodTypeAndCityContainingIgnoreCaseAndIsAvailable(
        UserRole role, BloodType bloodType, String city, Boolean isAvailable, Pageable pageable);

    @Query("SELECT u FROM User u WHERE u.autoScheduleEnabled = true")
    List<User> findByAutoScheduleEnabledTrue();
}
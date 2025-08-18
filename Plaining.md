Collecting workspace informationLooking at your repository structure and comparing it with your Plaining.md plan, I can see you've made good progress on the authentication and user management foundation. Let me provide a comprehensive step-by-step guide to complete the VitaSync Blood Bank Management System by tomorrow or day after tomorrow evening.

## 🧠 Current State Analysis

**✅ What's Already Done:**
- Basic Spring Boot setup with security configuration
- User authentication (JWT-based)
- User registration and management
- Basic entities and enums structure
- OpenAPI documentation setup

**❌ What's Missing (Critical for MVP):**
- Core business entities (TransfusionRequest, DonationResponse, BloodBank)
- Core business logic controllers and services
- Matching algorithms
- Dashboard APIs
- Database configuration for production
- Docker setup

## 🎯 Complete Implementation Roadmap

### **Day 1 (Today/Tomorrow): Complete Core Backend**

#### **Morning Session (4 hours)**

**1. Create Missing Entity Models (1 hour)**

````java
package com.vitasync.entity;

import com.vitasync.enums.BloodType;
import com.vitasync.enums.RequestStatus;
import com.vitasync.enums.Urgency;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "transfusion_requests")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TransfusionRequest {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "patient_id")
    private User patient;

    @NotBlank
    @Column(name = "patient_name")
    private String patientName;

    @Enumerated(EnumType.STRING)
    @Column(name = "blood_type", nullable = false)
    private BloodType bloodType;

    @Positive
    @Column(name = "units_required")
    private Integer unitsRequired;

    @Enumerated(EnumType.STRING)
    @Column(name = "urgency", nullable = false)
    private Urgency urgency;

    @NotBlank
    @Column(name = "hospital_name")
    private String hospitalName;

    @Column(name = "hospital_address")
    private String hospitalAddress;

    @Pattern(regexp = "\\+?[0-9]{10,15}")
    @Column(name = "contact_number")
    private String contactNumber;

    @Column(name = "medical_reason", length = 500)
    private String medicalReason;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private RequestStatus status = RequestStatus.PENDING;

    @Future
    @Column(name = "required_by_date")
    private LocalDateTime requiredByDate;

    @Column(name = "created_at")
    private LocalDateTime createdAt = LocalDateTime.now();

    @Column(name = "updated_at")
    private LocalDateTime updatedAt = LocalDateTime.now();

    @PreUpdate
    public void preUpdate() {
        this.updatedAt = LocalDateTime.now();
    }
}
````

````java
package com.vitasync.entity;

import com.vitasync.enums.DonationStatus;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "donation_responses")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class DonationResponse {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "donor_id", nullable = false)
    private User donor;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "request_id", nullable = false)
    private TransfusionRequest request;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private DonationStatus status;

    @Column(name = "donor_notes", length = 500)
    private String donorNotes;

    @Column(name = "scheduled_date")
    private LocalDateTime scheduledDate;

    @Column(name = "donation_date")
    private LocalDateTime donationDate;

    @Column(name = "created_at")
    private LocalDateTime createdAt = LocalDateTime.now();

    @Column(name = "updated_at")
    private LocalDateTime updatedAt = LocalDateTime.now();

    @PreUpdate
    public void preUpdate() {
        this.updatedAt = LocalDateTime.now();
    }
}
````

````java
package com.vitasync.entity;

import com.vitasync.enums.BloodType;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.util.Set;

@Entity
@Table(name = "blood_banks")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class BloodBank {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Column(name = "name", nullable = false)
    private String name;

    @NotBlank
    @Column(name = "address", nullable = false)
    private String address;

    @Column(name = "phone")
    private String phone;

    @ElementCollection(targetClass = BloodType.class)
    @Enumerated(EnumType.STRING)
    @CollectionTable(name = "blood_bank_available_types")
    @Column(name = "blood_type")
    private Set<BloodType> availableBloodTypes;

    @Column(name = "operating_hours")
    private String operatingHours;

    @Column(name = "latitude")
    private Double latitude;

    @Column(name = "longitude")
    private Double longitude;
}
````

**2. Create DTOs for Requests/Responses (30 minutes)**

````java
package com.vitasync.dto;

import com.vitasync.enums.BloodType;
import com.vitasync.enums.RequestStatus;
import com.vitasync.enums.Urgency;
import jakarta.validation.constraints.*;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class TransfusionRequestDto {
    private Long id;
    
    @NotBlank(message = "Patient name is required")
    private String patientName;
    
    @NotNull(message = "Blood type is required")
    private BloodType bloodType;
    
    @Positive(message = "Units required must be positive")
    private Integer unitsRequired;
    
    @NotNull(message = "Urgency is required")
    private Urgency urgency;
    
    @NotBlank(message = "Hospital name is required")
    private String hospitalName;
    
    private String hospitalAddress;
    
    @Pattern(regexp = "\\+?[0-9]{10,15}", message = "Invalid contact number")
    private String contactNumber;
    
    private String medicalReason;
    
    private RequestStatus status;
    
    @Future(message = "Required date must be in the future")
    private LocalDateTime requiredByDate;
    
    private LocalDateTime createdAt;
    private Integer responseCount;
    private Integer matchingDonorsCount;
}
````

````java
package com.vitasync.dto;

import com.vitasync.enums.DonationStatus;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class DonationResponseDto {
    private Long id;
    
    @NotNull(message = "Donor ID is required")
    private Long donorId;
    
    private String donorName;
    
    @NotNull(message = "Request ID is required")
    private Long requestId;
    
    @NotNull(message = "Status is required")
    private DonationStatus status;
    
    private String donorNotes;
    private LocalDateTime scheduledDate;
    private LocalDateTime donationDate;
    private LocalDateTime createdAt;
}
````

**3. Create Repositories (15 minutes)**

````java
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
}
````

````java
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
````

````java
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
````

#### **Afternoon Session (4 hours)**

**4. Create Core Services (2 hours)**

````java
package com.vitasync.services;

import com.vitasync.dto.TransfusionRequestDto;
import com.vitasync.entity.TransfusionRequest;
import com.vitasync.entity.User;
import com.vitasync.enums.BloodType;
import com.vitasync.enums.RequestStatus;
import com.vitasync.enums.Urgency;
import com.vitasync.repository.TransfusionRequestRepository;
import com.vitasync.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class TransfusionRequestService {
    
    private final TransfusionRequestRepository requestRepository;
    private final UserRepository userRepository;
    private final MatchingService matchingService;

    public TransfusionRequestDto createRequest(TransfusionRequestDto dto, Long patientId) {
        User patient = userRepository.findById(patientId)
            .orElseThrow(() -> new RuntimeException("Patient not found"));

        TransfusionRequest request = new TransfusionRequest();
        request.setPatient(patient);
        request.setPatientName(dto.getPatientName());
        request.setBloodType(dto.getBloodType());
        request.setUnitsRequired(dto.getUnitsRequired());
        request.setUrgency(dto.getUrgency());
        request.setHospitalName(dto.getHospitalName());
        request.setHospitalAddress(dto.getHospitalAddress());
        request.setContactNumber(dto.getContactNumber());
        request.setMedicalReason(dto.getMedicalReason());
        request.setRequiredByDate(dto.getRequiredByDate());
        request.setStatus(RequestStatus.PENDING);

        TransfusionRequest saved = requestRepository.save(request);
        
        // Auto-match with donors
        matchingService.autoMatchDonors(saved.getId());
        
        return convertToDto(saved);
    }

    public Page<TransfusionRequestDto> getRequests(
            RequestStatus status, Urgency urgency, BloodType bloodType, Pageable pageable) {
        
        Page<TransfusionRequest> requests;
        
        if (status != null && urgency != null && bloodType != null) {
            requests = requestRepository.findByStatusAndUrgencyAndBloodType(status, urgency, bloodType, pageable);
        } else if (status != null) {
            requests = requestRepository.findByStatus(status, pageable);
        } else if (bloodType != null) {
            requests = requestRepository.findByBloodType(bloodType, pageable);
        } else {
            requests = requestRepository.findAll(pageable);
        }
        
        return requests.map(this::convertToDto);
    }

    public TransfusionRequestDto getRequestById(Long id) {
        TransfusionRequest request = requestRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Request not found"));
        return convertToDto(request);
    }

    public List<TransfusionRequestDto> getUrgentRequests() {
        return requestRepository.findUrgentRequests().stream()
            .map(this::convertToDto)
            .toList();
    }

    public TransfusionRequestDto updateRequestStatus(Long id, RequestStatus status) {
        TransfusionRequest request = requestRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Request not found"));
        
        request.setStatus(status);
        request.setUpdatedAt(LocalDateTime.now());
        
        return convertToDto(requestRepository.save(request));
    }

    private TransfusionRequestDto convertToDto(TransfusionRequest request) {
        TransfusionRequestDto dto = new TransfusionRequestDto();
        dto.setId(request.getId());
        dto.setPatientName(request.getPatientName());
        dto.setBloodType(request.getBloodType());
        dto.setUnitsRequired(request.getUnitsRequired());
        dto.setUrgency(request.getUrgency());
        dto.setHospitalName(request.getHospitalName());
        dto.setHospitalAddress(request.getHospitalAddress());
        dto.setContactNumber(request.getContactNumber());
        dto.setMedicalReason(request.getMedicalReason());
        dto.setStatus(request.getStatus());
        dto.setRequiredByDate(request.getRequiredByDate());
        dto.setCreatedAt(request.getCreatedAt());
        return dto;
    }
}
````

````java
package com.vitasync.services;

import com.vitasync.dto.DonationResponseDto;
import com.vitasync.entity.DonationResponse;
import com.vitasync.entity.TransfusionRequest;
import com.vitasync.entity.User;
import com.vitasync.enums.DonationStatus;
import com.vitasync.repository.DonationResponseRepository;
import com.vitasync.repository.TransfusionRequestRepository;
import com.vitasync.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class DonationResponseService {
    
    private final DonationResponseRepository responseRepository;
    private final UserRepository userRepository;
    private final TransfusionRequestRepository requestRepository;

    public DonationResponseDto createResponse(DonationResponseDto dto) {
        User donor = userRepository.findById(dto.getDonorId())
            .orElseThrow(() -> new RuntimeException("Donor not found"));
        
        TransfusionRequest request = requestRepository.findById(dto.getRequestId())
            .orElseThrow(() -> new RuntimeException("Request not found"));

        DonationResponse response = new DonationResponse();
        response.setDonor(donor);
        response.setRequest(request);
        response.setStatus(dto.getStatus());
        response.setDonorNotes(dto.getDonorNotes());
        response.setScheduledDate(dto.getScheduledDate());

        DonationResponse saved = responseRepository.save(response);
        return convertToDto(saved);
    }

    public List<DonationResponseDto> getResponsesByRequest(Long requestId) {
        return responseRepository.findByRequestId(requestId).stream()
            .map(this::convertToDto)
            .toList();
    }

    public List<DonationResponseDto> getResponsesByDonor(Long donorId) {
        return responseRepository.findByDonorId(donorId).stream()
            .map(this::convertToDto)
            .toList();
    }

    public DonationResponseDto updateStatus(Long responseId, DonationStatus status) {
        DonationResponse response = responseRepository.findById(responseId)
            .orElseThrow(() -> new RuntimeException("Response not found"));
        
        response.setStatus(status);
        response.setUpdatedAt(LocalDateTime.now());
        
        if (status == DonationStatus.COMPLETED) {
            response.setDonationDate(LocalDateTime.now());
        }
        
        return convertToDto(responseRepository.save(response));
    }

    private DonationResponseDto convertToDto(DonationResponse response) {
        DonationResponseDto dto = new DonationResponseDto();
        dto.setId(response.getId());
        dto.setDonorId(response.getDonor().getId());
        dto.setDonorName(response.getDonor().getName());
        dto.setRequestId(response.getRequest().getId());
        dto.setStatus(response.getStatus());
        dto.setDonorNotes(response.getDonorNotes());
        dto.setScheduledDate(response.getScheduledDate());
        dto.setDonationDate(response.getDonationDate());
        dto.setCreatedAt(response.getCreatedAt());
        return dto;
    }
}
````

**5. Create Matching Service (1 hour)**

````java
package com.vitasync.services;

import com.vitasync.dto.DonorMatchDto;
import com.vitasync.entity.TransfusionRequest;
import com.vitasync.entity.User;
import com.vitasync.enums.BloodType;
import com.vitasync.enums.UserRole;
import com.vitasync.repository.TransfusionRequestRepository;
import com.vitasync.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class MatchingService {
    
    private final UserRepository userRepository;
    private final TransfusionRequestRepository requestRepository;
    
    public List<DonorMatchDto> findMatchingDonors(Long requestId) {
        TransfusionRequest request = requestRepository.findById(requestId)
            .orElseThrow(() -> new RuntimeException("Request not found"));
        
        Set<BloodType> compatibleTypes = getCompatibleBloodTypes(request.getBloodType());
        
        List<User> matchingDonors = userRepository.findAvailableDonorsByBloodTypes(compatibleTypes);
        
        return matchingDonors.stream()
            .map(donor -> createDonorMatch(donor, request))
            .toList();
    }
    
    public void autoMatchDonors(Long requestId) {
        List<DonorMatchDto> matches = findMatchingDonors(requestId);
        
        // Here you would implement notification logic
        // For now, we'll just log the matches
        System.out.println("Found " + matches.size() + " matching donors for request " + requestId);
    }
    
    private Set<BloodType> getCompatibleBloodTypes(BloodType requestedType) {
        return switch (requestedType) {
            case AB_POSITIVE -> Set.of(BloodType.AB_POSITIVE, BloodType.AB_NEGATIVE, 
                                     BloodType.A_POSITIVE, BloodType.A_NEGATIVE,
                                     BloodType.B_POSITIVE, BloodType.B_NEGATIVE,
                                     BloodType.O_POSITIVE, BloodType.O_NEGATIVE);
            case AB_NEGATIVE -> Set.of(BloodType.AB_NEGATIVE, BloodType.A_NEGATIVE,
                                      BloodType.B_NEGATIVE, BloodType.O_NEGATIVE);
            case A_POSITIVE -> Set.of(BloodType.A_POSITIVE, BloodType.A_NEGATIVE,
                                     BloodType.O_POSITIVE, BloodType.O_NEGATIVE);
            case A_NEGATIVE -> Set.of(BloodType.A_NEGATIVE, BloodType.O_NEGATIVE);
            case B_POSITIVE -> Set.of(BloodType.B_POSITIVE, BloodType.B_NEGATIVE,
                                     BloodType.O_POSITIVE, BloodType.O_NEGATIVE);
            case B_NEGATIVE -> Set.of(BloodType.B_NEGATIVE, BloodType.O_NEGATIVE);
            case O_POSITIVE -> Set.of(BloodType.O_POSITIVE, BloodType.O_NEGATIVE);
            case O_NEGATIVE -> Set.of(BloodType.O_NEGATIVE);
        };
    }
    
    private DonorMatchDto createDonorMatch(User donor, TransfusionRequest request) {
        DonorMatchDto match = new DonorMatchDto();
        match.setDonorId(donor.getId());
        match.setName(donor.getName());
        match.setBloodType(donor.getBloodType());
        match.setCity(donor.getCity());
        match.setLastDonationDate(donor.getLastDonationDate());
        match.setAvailable(donor.getIsAvailable());
        match.setMatchScore(calculateMatchScore(donor, request));
        return match;
    }
    
    private Integer calculateMatchScore(User donor, TransfusionRequest request) {
        int score = 50; // Base score
        
        // Exact blood type match
        if (donor.getBloodType() == request.getBloodType()) {
            score += 30;
        }
        
        // Recent donation penalty
        if (donor.getLastDonationDate() != null && 
            donor.getLastDonationDate().isAfter(LocalDate.now().minusDays(56))) {
            score -= 20;
        }
        
        // City match bonus
        if (donor.getCity() != null && request.getHospitalAddress() != null &&
            request.getHospitalAddress().contains(donor.getCity())) {
            score += 20;
        }
        
        return Math.max(0, Math.min(100, score));
    }
}
````

**6. Create Controllers (1 hour)**

````java
package com.vitasync.controllers;

import com.vitasync.dto.TransfusionRequestDto;
import com.vitasync.enums.BloodType;
import com.vitasync.enums.RequestStatus;
import com.vitasync.enums.Urgency;
import com.vitasync.services.TransfusionRequestService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/transfusion-requests")
@RequiredArgsConstructor
@Tag(name = "Transfusion Requests", description = "Blood transfusion request management")
public class TransfusionRequestController {
    
    private final TransfusionRequestService requestService;

    @PostMapping
    @Operation(summary = "Create a new transfusion request")
    public ResponseEntity<TransfusionRequestDto> createRequest(
            @Valid @RequestBody TransfusionRequestDto requestDto,
            @RequestParam Long patientId) {
        
        TransfusionRequestDto created = requestService.createRequest(requestDto, patientId);
        return new ResponseEntity<>(created, HttpStatus.CREATED);
    }

    @GetMapping
    @Operation(summary = "Get transfusion requests with filters")
    public ResponseEntity<Page<TransfusionRequestDto>> getRequests(
            @RequestParam(required = false) RequestStatus status,
            @RequestParam(required = false) Urgency urgency,
            @RequestParam(required = false) BloodType bloodType,
            Pageable pageable) {
        
        Page<TransfusionRequestDto> requests = requestService.getRequests(status, urgency, bloodType, pageable);
        return ResponseEntity.ok(requests);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get transfusion request by ID")
    public ResponseEntity<TransfusionRequestDto> getRequestById(@PathVariable Long id) {
        TransfusionRequestDto request = requestService.getRequestById(id);
        return ResponseEntity.ok(request);
    }

    @GetMapping("/urgent")
    @Operation(summary = "Get urgent transfusion requests")
    public ResponseEntity<List<TransfusionRequestDto>> getUrgentRequests() {
        List<TransfusionRequestDto> requests = requestService.getUrgentRequests();
        return ResponseEntity.ok(requests);
    }

    @PutMapping("/{id}/status")
    @Operation(summary = "Update request status")
    public ResponseEntity<TransfusionRequestDto> updateStatus(
            @PathVariable Long id,
            @RequestParam RequestStatus status) {
        
        TransfusionRequestDto updated = requestService.updateRequestStatus(id, status);
        return ResponseEntity.ok(updated);
    }
}
````

### **Day 2: Dashboard, Docker & Deployment**

#### **Morning Session (4 hours)**

**7. Create Dashboard Service & Controller (1.5 hours)**

````java
package com.vitasync.services;

import com.vitasync.dto.DashboardStatsDto;
import com.vitasync.enums.BloodType;
import com.vitasync.enums.RequestStatus;
import com.vitasync.enums.UserRole;
import com.vitasync.repository.DonationResponseRepository;
import com.vitasync.repository.TransfusionRequestRepository;
import com.vitasync.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DashboardService {
    
    private final UserRepository userRepository;
    private final TransfusionRequestRepository requestRepository;
    private final DonationResponseRepository responseRepository;

    public DashboardStatsDto getDashboardStats() {
        DashboardStatsDto stats = new DashboardStatsDto();
        
        stats.setTotalDonors(userRepository.countByRole(UserRole.DONOR));
        stats.setActiveDonors(userRepository.countByRoleAndIsAvailable(UserRole.DONOR, true));
        stats.setTotalRequests(requestRepository.count());
        stats.setUrgentRequests(requestRepository.countByStatus(RequestStatus.PENDING));
        stats.setSuccessfulMatches(responseRepository.countSuccessfulDonations());
        
        // Calculate success rate
        long totalRequests = stats.getTotalRequests();
        if (totalRequests > 0) {
            stats.setSuccessRate((double) stats.getSuccessfulMatches() / totalRequests * 100);
        } else {
            stats.setSuccessRate(0.0);
        }
        
        // Blood type distribution
        Map<BloodType, Long> distribution = userRepository.countDonorsByBloodType()
            .stream()
            .collect(Collectors.toMap(
                result -> (BloodType) result[0],
                result -> (Long) result[1]
            ));
        stats.setBloodTypeDistribution(distribution);
        
        return stats;
    }
}
````

**8. Update Database Configuration (30 minutes)**

````properties
# Application Configuration
spring.application.name=VitaSync Blood Bank Management System
server.port=8080

# Database Configuration
spring.datasource.url=${DATABASE_URL:jdbc:h2:mem:testdb}
spring.datasource.driver-class-name=${DATABASE_DRIVER:org.h2.Driver}
spring.datasource.username=${DATABASE_USERNAME:sa}
spring.datasource.password=${DATABASE_PASSWORD:}

# JPA Configuration
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
spring.jpa.properties.hibernate.format_sql=true
spring.jpa.database-platform=${DATABASE_PLATFORM:org.hibernate.dialect.H2Dialect}

# H2 Console (for development)
spring.h2.console.enabled=true
spring.h2.console.path=/h2-console

# JWT Configuration
jwt.secret=${JWT_SECRET:mySecretKey}
jwt.expiration=${JWT_EXPIRATION:86400000}

# OpenAPI Configuration
springdoc.api-docs.path=/api-docs
springdoc.swagger-ui.path=/swagger-ui.html
springdoc.swagger-ui.operationsSorter=method

# Logging
logging.level.com.vitasync=DEBUG
logging.level.org.springframework.security=DEBUG
````

````properties
# Production Database Configuration
spring.datasource.url=${DATABASE_URL}
spring.datasource.driver-class-name=org.postgresql.Driver
spring.datasource.username=${DATABASE_USERNAME}
spring.datasource.password=${DATABASE_PASSWORD}

# JPA Configuration for Production
spring.jpa.hibernate.ddl-auto=validate
spring.jpa.show-sql=false
spring.jpa.database-platform=org.hibernate.dialect.PostgreSQLDialect

# Disable H2 Console in production
spring.h2.console.enabled=false

# Logging for Production
logging.level.com.vitasync=INFO
logging.level.org.springframework.security=WARN
````

**9. Create Docker Configuration (1 hour)**

````dockerfile
FROM openjdk:21-jre-slim

# Set working directory
WORKDIR /app

# Copy the jar file
COPY target/vitasync-*.jar app.jar

# Expose port
EXPOSE 8080

# Add health check
HEALTHCHECK --interval=30s --timeout=3s --start-period=30s --retries=3 \
  CMD curl -f http://localhost:8080/actuator/health || exit 1

# Run the application
ENTRYPOINT ["java", "-jar", "/app.jar"]
````

````yaml
version: '3.8'

services:
  postgres:
    image: postgres:15
    environment:
      POSTGRES_DB: vitasync
      POSTGRES_USER: vitasync
      POSTGRES_PASSWORD: vitasync123
    ports:
      - "5432:5432"
    volumes:
      - postgres_data:/var/lib/postgresql/data
    networks:
      - vitasync-network

  vitasync-app:
    build: .
    ports:
      - "8080:8080"
    environment:
      DATABASE_URL: jdbc:postgresql://postgres:5432/vitasync
      DATABASE_DRIVER: org.postgresql.Driver
      DATABASE_USERNAME: vitasync
      DATABASE_PASSWORD: vitasync123
      DATABASE_PLATFORM: org.hibernate.dialect.PostgreSQLDialect
      JWT_SECRET: myVerySecretJWTKey123456789
      SPRING_PROFILES_ACTIVE: prod
    depends_on:
      - postgres
    networks:
      - vitasync-network

volumes:
  postgres_data:

networks:
  vitasync-network:
    driver: bridge
````

**10. Add Missing Dependencies (15 minutes)**

````xml
plugins {
    id 'java'
    id 'org.springframework.boot' version '3.2.0'
    id 'io.spring.dependency-management' version '1.1.4'
}

group = 'com.vitasync'
version = '0.0.1-SNAPSHOT'

java {
    sourceCompatibility = '21'
}

configurations {
    compileOnly {
        extendsFrom annotationProcessor
    }
}

repositories {
    mavenCentral()
}

dependencies {
    implementation 'org.springframework.boot:spring-boot-starter-data-jpa'
    implementation 'org.springframework.boot:spring-boot-starter-security'
    implementation 'org.springframework.boot:spring-boot-starter-validation'
    implementation 'org.springframework.boot:spring-boot-starter-web'
    implementation 'org.springframework.boot:spring-boot-starter-actuator'
    
    // JWT
    implementation 'io.jsonwebtoken:jjwt-api:0.11.5'
    runtimeOnly 'io.jsonwebtoken:jjwt-impl:0.11.5'
    runtimeOnly 'io.jsonwebtoken:jjwt-jackson:0.11.5'
    
    // OpenAPI Documentation
    implementation 'org.springdoc:springdoc-openapi-starter-webmvc-ui:2.2.0'
    
    // Database
    runtimeOnly 'com.h2database:h2'
    runtimeOnly 'org.postgresql:postgresql'
    
    // Lombok
    compileOnly 'org.projectlombok:lombok'
    annotationProcessor 'org.projectlombok:lombok'
    
    // Testing
    testImplementation 'org.springframework.boot:spring-boot-starter-test'
    testImplementation 'org.springframework.security:spring-security-test'
}

tasks.named('test') {
    useJUnitPlatform()
}
````

#### **Afternoon Session (3 hours)**

**11. Complete Missing DTOs & Controllers (1 hour)**

````java
package com.vitasync.dto;

import com.vitasync.enums.BloodType;
import lombok.Data;

import java.time.LocalDate;

@Data
public class DonorMatchDto {
    private Long donorId;
    private String name;
    private BloodType bloodType;
    private String city;
    private LocalDate lastDonationDate;
    private String distance;
    private Boolean isAvailable;
    private Integer matchScore;
}
````

````java
package com.vitasync.dto;

import com.vitasync.enums.BloodType;
import lombok.Data;

import java.util.Map;

@Data
public class DashboardStatsDto {
    private Long totalDonors;
    private Long activeDonors;
    private Long totalRequests;
    private Long urgentRequests;
    private Long successfulMatches;
    private Double successRate;
    private Map<BloodType, Long> bloodTypeDistribution;
}
````

**12. Add Missing Repository Methods (30 minutes)**

````java
package com.vitasync.repository;

import com.vitasync.entity.User;
import com.vitasync.enums.BloodType;
import com.vitasync.enums.UserRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface UserRepository extends JpaRepository<User, Long> {
    
    Optional<User> findByEmail(String email);
    
    boolean existsByEmail(String email);
    
    List<User> findByRoleAndIsAvailableAndBloodTypeIn(
        UserRole role, Boolean isAvailable, Set<BloodType> bloodTypes);
    
    @Query("SELECT u FROM User u WHERE u.role = 'DONOR' AND u.isAvailable = true AND u.bloodType IN :bloodTypes")
    List<User> findAvailableDonorsByBloodTypes(@Param("bloodTypes") Set<BloodType> bloodTypes);
    
    @Query("SELECT COUNT(u) FROM User u WHERE u.role = :role")
    Long countByRole(@Param("role") UserRole role);
    
    @Query("SELECT COUNT(u) FROM User u WHERE u.role = :role AND u.isAvailable = :isAvailable")
    Long countByRoleAndIsAvailable(@Param("role") UserRole role, @Param("isAvailable") Boolean isAvailable);
    
    @Query("SELECT u.bloodType, COUNT(u) FROM User u WHERE u.role = 'DONOR' GROUP BY u.bloodType")
    List<Object[]> countDonorsByBloodType();
}
````

**13. Final Testing & Documentation (1.5 hours)**

Create comprehensive testing and documentation:

````java
package com.vitasync;

import com.vitasync.controllers.TransfusionRequestController;
import com.vitasync.services.TransfusionRequestService;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.beans.factory.annotation.Autowired;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(TransfusionRequestController.class)
class TransfusionRequestControllerTest {
    
    @Autowired
    private MockMvc mockMvc;
    
    @MockBean
    private TransfusionRequestService requestService;
    
    @Test
    void shouldReturnUrgentRequests() throws Exception {
        mockMvc.perform(get("/api/transfusion-requests/urgent"))
                .andExpect(status().isOk());
    }
}
````

## 🚀 Quick Deployment Commands

**Build and Test:**
````bash
./gradlew clean build
./gradlew test
````

**Docker Commands:**
````bash
# Build the application
./gradlew clean package

# Build Docker image
docker build -t vitasync-app .

# Run with Docker Compose
docker-compose up -d

# Check logs
docker-compose logs -f vitasync-app
````

## 🎯 Final Checklist

**By Tomorrow Evening, you should have:**
- ✅ Complete REST API with all endpoints from Plaining.md
- ✅ Working authentication system
- ✅ Blood donation matching algorithm
- ✅ Dashboard with statistics
- ✅ Dockerized application
- ✅ Production-ready database configuration
- ✅ Swagger API documentation at `/swagger-ui.html`
- ✅ Health checks and monitoring endpoints

**Access Points:**
- API Documentation: `http://localhost:8080/swagger-ui.html`
- H2 Console (dev): `http://localhost:8080/h2-console`
- Health Check: `http://localhost:8080/actuator/health`

This roadmap will get you a fully functional VitaSync Blood Bank Management System ready for Azure deployment by tomorrow evening!

Similar code found with 3 license types
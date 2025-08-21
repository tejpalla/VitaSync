package com.vitasync.services;

import com.vitasync.dto.DonorMatchDto;
import com.vitasync.dto.TransfusionRequestDto;
import com.vitasync.entity.TransfusionRequest;
import com.vitasync.entity.User;
import com.vitasync.enums.BloodType;
import com.vitasync.enums.RequestStatus;
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

    public List<TransfusionRequestDto> findSuitableRequests(Long donorId) {
        User donor = userRepository.findById(donorId)
            .orElseThrow(() -> new RuntimeException("Donor not found"));
        
        Set<BloodType> donorCompatibleTypes = getRequestCompatibleTypes(donor.getBloodType());
        
        return requestRepository.findAll().stream()
            .filter(request -> request.getStatus() == RequestStatus.PENDING)
            .filter(request -> donorCompatibleTypes.contains(request.getBloodType()))
            .map(this::convertRequestToDto)
            .toList();
    }

    private Set<BloodType> getRequestCompatibleTypes(BloodType donorBloodType) {
    // Reverse compatibility - what requests can this donor fulfill
        return switch (donorBloodType) {
            case O_NEGATIVE -> Set.of(BloodType.O_NEGATIVE, BloodType.O_POSITIVE,
                                    BloodType.A_NEGATIVE, BloodType.A_POSITIVE,
                                    BloodType.B_NEGATIVE, BloodType.B_POSITIVE,
                                    BloodType.AB_NEGATIVE, BloodType.AB_POSITIVE);
            case O_POSITIVE -> Set.of(BloodType.O_POSITIVE, BloodType.A_POSITIVE,
                                    BloodType.B_POSITIVE, BloodType.AB_POSITIVE);
            case A_NEGATIVE -> Set.of(BloodType.A_NEGATIVE, BloodType.A_POSITIVE,
                                    BloodType.AB_NEGATIVE, BloodType.AB_POSITIVE);
            case A_POSITIVE -> Set.of(BloodType.A_POSITIVE, BloodType.AB_POSITIVE);
            case B_NEGATIVE -> Set.of(BloodType.B_NEGATIVE, BloodType.B_POSITIVE,
                                    BloodType.AB_NEGATIVE, BloodType.AB_POSITIVE);
            case B_POSITIVE -> Set.of(BloodType.B_POSITIVE, BloodType.AB_POSITIVE);
            case AB_NEGATIVE -> Set.of(BloodType.AB_NEGATIVE, BloodType.AB_POSITIVE);
            case AB_POSITIVE -> Set.of(BloodType.AB_POSITIVE);
        };
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
        match.setIsAvailable(donor.getIsAvailable());
        match.setMatchScore(calculateMatchScore(donor, request));
        return match;
    }

    private TransfusionRequestDto convertRequestToDto(TransfusionRequest request) {
        TransfusionRequestDto dto = new TransfusionRequestDto();
        dto.setId(request.getId());
        dto.setPatientName(request.getPatientName());
        dto.setBloodType(request.getBloodType());
        dto.setUnitsRequired(request.getUnitsRequired());
        dto.setUrgency(request.getUrgency());
        dto.setHospitalName(request.getHospitalName());
        dto.setRequiredByDate(request.getRequiredByDate());
        dto.setCreatedAt(request.getCreatedAt());
        return dto;
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
package com.vitasync.services;

import com.vitasync.dto.DashboardStatsDto;
import com.vitasync.dto.TransfusionRequestDto;
import com.vitasync.enums.BloodType;
import com.vitasync.enums.RequestStatus;
import com.vitasync.enums.UserRole;
import com.vitasync.repository.DonationResponseRepository;
import com.vitasync.repository.TransfusionRequestRepository;
import com.vitasync.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class DashboardService {
    
    private final UserRepository userRepository;
    private final TransfusionRequestRepository requestRepository;
    private final DonationResponseRepository responseRepository;
    private final TransfusionRequestService requestService;

    public DashboardStatsDto getDashboardStats() {
        DashboardStatsDto stats = new DashboardStatsDto();
        
        // Basic counts
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
        Map<BloodType, Long> distribution = new HashMap<>();
        List<Object[]> results = userRepository.countDonorsByBloodType();
        
        for (Object[] result : results) {
            BloodType bloodType = (BloodType) result[0];
            Long count = (Long) result[1];
            distribution.put(bloodType, count);
        }
        stats.setBloodTypeDistribution(distribution);
        
        return stats;
    }
    
    public List<TransfusionRequestDto> getUrgentRequests() {
        return requestService.getUrgentRequests();
    }
    
    public List<TransfusionRequestDto> getRecentRequests() {
        LocalDateTime yesterday = LocalDateTime.now().minusDays(1);
        return requestRepository.findRecentRequests(yesterday).stream()
            .map(requestService::convertToDto)
            .toList();
    }
}
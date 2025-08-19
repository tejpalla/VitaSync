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
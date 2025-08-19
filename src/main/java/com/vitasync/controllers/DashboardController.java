package com.vitasync.controllers;

import com.vitasync.dto.DashboardStatsDto;
import com.vitasync.dto.TransfusionRequestDto;
import com.vitasync.services.DashboardService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/dashboard")
@RequiredArgsConstructor
@Tag(name = "Dashboard", description = "Dashboard and analytics endpoints")
public class DashboardController {
    
    private final DashboardService dashboardService;

    @GetMapping("/stats")
    @Operation(summary = "Get dashboard statistics")
    public ResponseEntity<DashboardStatsDto> getDashboardStats() {
        DashboardStatsDto stats = dashboardService.getDashboardStats();
        return ResponseEntity.ok(stats);
    }

    @GetMapping("/urgent-requests")
    @Operation(summary = "Get urgent blood requests")
    public ResponseEntity<List<TransfusionRequestDto>> getUrgentRequests() {
        List<TransfusionRequestDto> urgentRequests = dashboardService.getUrgentRequests();
        return ResponseEntity.ok(urgentRequests);
    }

    @GetMapping("/recent-activities")
    @Operation(summary = "Get recent activities")
    public ResponseEntity<List<TransfusionRequestDto>> getRecentActivities() {
        List<TransfusionRequestDto> recentRequests = dashboardService.getRecentRequests();
        return ResponseEntity.ok(recentRequests);
    }
}
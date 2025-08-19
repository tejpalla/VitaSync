package com.vitasync.controllers;

import com.vitasync.dto.DonorMatchDto;
import com.vitasync.dto.TransfusionRequestDto;
import com.vitasync.services.MatchingService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/matching")
@RequiredArgsConstructor
@Tag(name = "Matching", description = "Donor-Patient matching endpoints")
public class MatchingController {
    
    private final MatchingService matchingService;

    @GetMapping("/donors-for-request/{requestId}")
    @Operation(summary = "Find matching donors for a request")
    public ResponseEntity<Map<String, Object>> getDonorsForRequest(@PathVariable Long requestId) {
        List<DonorMatchDto> matches = matchingService.findMatchingDonors(requestId);
        
        Map<String, Object> response = Map.of(
            "requestId", requestId,
            "matchingDonors", matches,
            "totalMatches", matches.size()
        );
        
        return ResponseEntity.ok(response);
    }

    @GetMapping("/requests-for-donor/{donorId}")
    @Operation(summary = "Find suitable requests for a donor")
    public ResponseEntity<List<TransfusionRequestDto>> getRequestsForDonor(@PathVariable Long donorId) {
        List<TransfusionRequestDto> requests = matchingService.findSuitableRequests(donorId);
        return ResponseEntity.ok(requests);
    }

    @PostMapping("/auto-match/{requestId}")
    @Operation(summary = "Auto-match donors to a request")
    public ResponseEntity<Map<String, Object>> autoMatchDonors(@PathVariable Long requestId) {
        matchingService.autoMatchDonors(requestId);
        
        Map<String, Object> response = Map.of(
            "requestId", requestId,
            "message", "Auto-matching initiated successfully"
        );
        
        return ResponseEntity.ok(response);
    }
}
package com.vitasync.controllers;

import com.vitasync.dto.DonationResponseDto;
import com.vitasync.enums.DonationStatus;
import com.vitasync.services.DonationResponseService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/donation-responses")
@RequiredArgsConstructor
@Tag(name = "Donation Responses", description = "Donation response management")
public class DonationResponseController {
    
    private final DonationResponseService responseService;

    @PostMapping
    @Operation(summary = "Create a donation response")
    public ResponseEntity<DonationResponseDto> createResponse(@Valid @RequestBody DonationResponseDto responseDto) {
        DonationResponseDto created = responseService.createResponse(responseDto);
        return new ResponseEntity<>(created, HttpStatus.CREATED);
    }

    @GetMapping("/request/{requestId}")
    @Operation(summary = "Get responses by request ID")
    public ResponseEntity<List<DonationResponseDto>> getResponsesByRequest(@PathVariable Long requestId) {
        List<DonationResponseDto> responses = responseService.getResponsesByRequest(requestId);
        return ResponseEntity.ok(responses);
    }

    @GetMapping("/donor/{donorId}")
    @Operation(summary = "Get responses by donor ID")
    public ResponseEntity<List<DonationResponseDto>> getResponsesByDonor(@PathVariable Long donorId) {
        List<DonationResponseDto> responses = responseService.getResponsesByDonor(donorId);
        return ResponseEntity.ok(responses);
    }

    @PutMapping("/{id}/status")
    @Operation(summary = "Update response status")
    public ResponseEntity<DonationResponseDto> updateStatus(
            @PathVariable Long id,
            @RequestParam DonationStatus status) {
        
        DonationResponseDto updated = responseService.updateStatus(id, status);
        return ResponseEntity.ok(updated);
    }
}
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
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
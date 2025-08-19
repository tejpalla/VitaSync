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
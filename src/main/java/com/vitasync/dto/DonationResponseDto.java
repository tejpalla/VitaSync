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
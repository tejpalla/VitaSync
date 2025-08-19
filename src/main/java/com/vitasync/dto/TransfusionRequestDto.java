package com.vitasync.dto;

import com.vitasync.enums.BloodType;
import com.vitasync.enums.RequestStatus;
import com.vitasync.enums.Urgency;
import jakarta.validation.constraints.*;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class TransfusionRequestDto {
    private Long id;
    
    @NotBlank(message = "Patient name is required")
    private String patientName;
    
    @NotNull(message = "Blood type is required")
    private BloodType bloodType;
    
    @Positive(message = "Units required must be positive")
    private Integer unitsRequired;
    
    @NotNull(message = "Urgency is required")
    private Urgency urgency;
    
    @NotBlank(message = "Hospital name is required")
    private String hospitalName;
    
    private String hospitalAddress;
    
    @Pattern(regexp = "\\+?[0-9]{10,15}", message = "Invalid contact number")
    private String contactNumber;
    
    private String medicalReason;
    
    private RequestStatus status;
    
    @Future(message = "Required date must be in the future")
    private LocalDateTime requiredByDate;
    
    private LocalDateTime createdAt;
    private Integer responseCount;
    private Integer matchingDonorsCount;
    
}
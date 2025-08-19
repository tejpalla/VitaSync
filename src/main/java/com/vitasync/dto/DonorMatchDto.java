package com.vitasync.dto;

import com.vitasync.enums.BloodType;
import lombok.Data;

import java.time.LocalDate;

@Data
public class DonorMatchDto {
    private Long donorId;
    private String name;
    private BloodType bloodType;
    private String city;
    private LocalDate lastDonationDate;
    private String distance;
    private Boolean isAvailable;
    private Integer matchScore;
}
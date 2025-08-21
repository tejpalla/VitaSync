package com.vitasync.services.scheduler;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import com.vitasync.enums.Urgency;

import com.vitasync.dto.TransfusionRequestDto;
import com.vitasync.entity.User;
import com.vitasync.enums.RequestStatus;
import com.vitasync.repository.UserRepository;
import com.vitasync.services.TransfusionRequestService;

@Component
public class AutoTransfusionScheduler {

    private final UserRepository userRepository;
    private final TransfusionRequestService transfusionRequestService;

    public AutoTransfusionScheduler(UserRepository userRepository,
                                    TransfusionRequestService transfusionRequestService) {
        this.userRepository = userRepository;
        this.transfusionRequestService = transfusionRequestService;
    }

    /**
     * Scheduler runs every day at 6 AM
     */
    @Scheduled(cron = "0 0 6 * * ?")
    public void autoScheduleTransfusions() {
        List<User> patients = userRepository.findByAutoScheduleEnabledTrue();

        for (User patient : patients) {
            if (shouldSchedule(patient)) {
                TransfusionRequestDto dto = buildDtoFromPatient(patient);
                transfusionRequestService.createRequest(dto, patient.getId());

                // update next transfusion date
                if (patient.getTransfusionFrequencyDays() != null) {
                    patient.setNextTransfusionDate(
                        patient.getNextTransfusionDate()
                               .plusDays(patient.getTransfusionFrequencyDays())
                    );
                    userRepository.save(patient);
                }
            }
        }
    }

    private boolean shouldSchedule(User patient) {
        LocalDate today = LocalDate.now();
        return Boolean.TRUE.equals(patient.getAutoScheduleEnabled())
                && patient.getNextTransfusionDate() != null
                && !patient.getNextTransfusionDate().isAfter(today);
    }

    private TransfusionRequestDto buildDtoFromPatient(User patient) {
        TransfusionRequestDto dto = new TransfusionRequestDto();
        dto.setPatientName(patient.getName());
        dto.setBloodType(patient.getBloodType());
        dto.setUnitsRequired(1); // default for MVP
        dto.setUrgency(Urgency.ROUTINE);
        dto.setHospitalName(patient.getPreferredHospitalName());
        dto.setHospitalAddress(patient.getPreferredHospitalAddress());
        dto.setContactNumber(patient.getPhone());
        dto.setMedicalReason("Automated scheduled transfusion");
        dto.setStatus(RequestStatus.PENDING);
        dto.setRequiredByDate(patient.getNextTransfusionDate().atStartOfDay());
        dto.setCreatedAt(LocalDateTime.now());
        dto.setResponseCount(0);
        dto.setMatchingDonorsCount(0);
        return dto;
    }
}

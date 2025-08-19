package com.vitasync.entity;

import com.vitasync.enums.BloodType;
import com.vitasync.enums.RequestStatus;
import com.vitasync.enums.Urgency;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "transfusion_requests")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TransfusionRequest {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "patient_id")
    private User patient;

    @NotBlank
    @Column(name = "patient_name")
    private String patientName;

    @Enumerated(EnumType.STRING)
    @Column(name = "blood_type", nullable = false)
    private BloodType bloodType;

    @Positive
    @Column(name = "units_required")
    private Integer unitsRequired;

    @Enumerated(EnumType.STRING)
    @Column(name = "urgency", nullable = false)
    private Urgency urgency;

    @NotBlank
    @Column(name = "hospital_name")
    private String hospitalName;

    @Column(name = "hospital_address")
    private String hospitalAddress;

    @Pattern(regexp = "\\+?[0-9]{10,15}")
    @Column(name = "contact_number")
    private String contactNumber;

    @Column(name = "medical_reason", length = 500)
    private String medicalReason;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private RequestStatus status = RequestStatus.PENDING;

    @Future
    @Column(name = "required_by_date")
    private LocalDateTime requiredByDate;

    @Column(name = "created_at")
    private LocalDateTime createdAt = LocalDateTime.now();

    @Column(name = "updated_at")
    private LocalDateTime updatedAt = LocalDateTime.now();

    @PreUpdate
    public void preUpdate() {
        this.updatedAt = LocalDateTime.now();
    }
}
package com.vitasync.entity;

import com.vitasync.enums.BloodType;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.util.Set;

@Entity
@Table(name = "blood_banks")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class BloodBank {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Column(name = "name", nullable = false)
    private String name;

    @NotBlank
    @Column(name = "address", nullable = false)
    private String address;

    @Column(name = "phone")
    private String phone;

    @ElementCollection(targetClass = BloodType.class)
    @Enumerated(EnumType.STRING)
    @CollectionTable(name = "blood_bank_available_types")
    @Column(name = "blood_type")
    private Set<BloodType> availableBloodTypes;

    @Column(name = "operating_hours")
    private String operatingHours;

    @Column(name = "latitude")
    private Double latitude;

    @Column(name = "longitude")
    private Double longitude;
}
package com.vitasync.entity;

import java.time.LocalDate;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import com.vitasync.enums.BloodType;
import com.vitasync.enums.UserRole;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Entity representing a user in the VitaSync system.
 * Users can be donors, recipients, or administrators.
 */
@Entity
@Table(name = "users")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Name is required")
    private String name; // NEW FIELD - SAFE TO ADD

    @NotBlank(message = "Email is required")
    @Email(message = "Please provide a valid email address")
    private String email;

    @NotBlank(message = "Phone number is required")
    @Pattern(regexp = "^\\+?[1-9][0-9]{7,14}$", message = "Please provide a valid phone number")
    private String phone;

    @NotBlank(message = "Password is required")
    @Size(min = 8, message = "Password must be at least 8 characters long")
    private String password;

    // NEW FIELD - SAFE TO ADD
    private String city; // Optional field for location matching

    // NEW FIELD - SAFE TO ADD  
    private String address; // Optional field for detailed location

    @Enumerated(EnumType.STRING)
    private BloodType bloodType; // Optional during registration

    @NotNull(message = "Role is required")
    @Enumerated(EnumType.STRING)
    private UserRole role;  //    DONOR, RECIPIENT, ADMIN, DOCTOR

    private Double longitude; // Optional - can be set later

    private Double latitude; // Optional - can be set later

    private Boolean isAvailable = true;

    private LocalDate lastDonationDate;

    @CreationTimestamp
    private LocalDate createdAt;

    @UpdateTimestamp
    private LocalDate updatedAt;

    // Inside User.java
    // Fields only relevant if role == RECIPIENT
    private Integer transfusionFrequencyDays;  // e.g. every 15 days
    
    private LocalDate nextTransfusionDate;     // auto-calculated after each transfusion
    
    private Boolean autoScheduleEnabled = false; // toggle automation
}
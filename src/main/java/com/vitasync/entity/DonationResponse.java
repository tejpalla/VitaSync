package com.vitasync.entity;

import com.vitasync.enums.DonationStatus;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "donation_responses")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class DonationResponse {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "donor_id", nullable = false)
    private User donor;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "request_id", nullable = false)
    private TransfusionRequest request;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private DonationStatus status;

    @Column(name = "donor_notes", length = 500)
    private String donorNotes;

    @Column(name = "scheduled_date")
    private LocalDateTime scheduledDate;

    @Column(name = "donation_date")
    private LocalDateTime donationDate;

    @Column(name = "created_at")
    private LocalDateTime createdAt = LocalDateTime.now();

    @Column(name = "updated_at")
    private LocalDateTime updatedAt = LocalDateTime.now();

    @PreUpdate
    public void preUpdate() {
        this.updatedAt = LocalDateTime.now();
    }
}
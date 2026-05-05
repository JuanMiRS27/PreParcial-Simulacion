package com.example.preparcialarle.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "evaluation_parameters")
public class EvaluationParameters {
    @Id
    private Long id;

    @Column(nullable = false)
    private Integer lowAmountThreshold;

    @Column(nullable = false)
    private Integer mediumAmountThreshold;

    @Column(nullable = false)
    private Integer robberyReviewThreshold;

    @Column(nullable = false)
    private Integer vehicleAutoApproveThreshold;

    @Column(nullable = false)
    private Integer minDescriptionLength;

    @Column(nullable = false)
    private LocalDateTime updatedAt;

    @PrePersist
    @PreUpdate
    public void touch() {
        updatedAt = LocalDateTime.now();
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Integer getLowAmountThreshold() { return lowAmountThreshold; }
    public void setLowAmountThreshold(Integer lowAmountThreshold) { this.lowAmountThreshold = lowAmountThreshold; }
    public Integer getMediumAmountThreshold() { return mediumAmountThreshold; }
    public void setMediumAmountThreshold(Integer mediumAmountThreshold) { this.mediumAmountThreshold = mediumAmountThreshold; }
    public Integer getRobberyReviewThreshold() { return robberyReviewThreshold; }
    public void setRobberyReviewThreshold(Integer robberyReviewThreshold) { this.robberyReviewThreshold = robberyReviewThreshold; }
    public Integer getVehicleAutoApproveThreshold() { return vehicleAutoApproveThreshold; }
    public void setVehicleAutoApproveThreshold(Integer vehicleAutoApproveThreshold) { this.vehicleAutoApproveThreshold = vehicleAutoApproveThreshold; }
    public Integer getMinDescriptionLength() { return minDescriptionLength; }
    public void setMinDescriptionLength(Integer minDescriptionLength) { this.minDescriptionLength = minDescriptionLength; }
    public LocalDateTime getUpdatedAt() { return updatedAt; }
}

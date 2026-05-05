package com.example.preparcialarle.dto.admin;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public record EvaluationParametersRequest(
        @NotNull @Min(1) Integer lowAmountThreshold,
        @NotNull @Min(1) Integer mediumAmountThreshold,
        @NotNull @Min(1) Integer robberyReviewThreshold,
        @NotNull @Min(1) Integer vehicleAutoApproveThreshold,
        @NotNull @Min(5) Integer minDescriptionLength
) {}

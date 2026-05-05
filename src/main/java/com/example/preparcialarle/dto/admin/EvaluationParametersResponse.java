package com.example.preparcialarle.dto.admin;

import java.time.LocalDateTime;

public record EvaluationParametersResponse(
        Integer lowAmountThreshold,
        Integer mediumAmountThreshold,
        Integer robberyReviewThreshold,
        Integer vehicleAutoApproveThreshold,
        Integer minDescriptionLength,
        LocalDateTime updatedAt
) {}

package com.example.preparcialarle.dto.evaluation;

import jakarta.validation.constraints.NotNull;

public record AdminDecisionRequest(
        @NotNull Boolean aprobado,
        String motivo
) {}

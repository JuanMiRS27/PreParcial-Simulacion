package com.example.preparcialarle.dto.evaluation;

import com.example.preparcialarle.model.ClaimStatus;
import java.time.LocalDateTime;

public record EvaluationResponse(
        Long id,
        Long claimId,
        ClaimStatus resultado,
        Integer puntajeRiesgo,
        String motivo,
        LocalDateTime fechaEvaluacion
) {}

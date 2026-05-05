package com.example.preparcialarle.dto.claim;

import com.example.preparcialarle.model.ClaimStatus;
import com.example.preparcialarle.model.ClaimType;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

public record ClaimResponse(
        Long id,
        ClaimType tipoSiniestro,
        String descripcion,
        BigDecimal valorEstimado,
        String ubicacion,
        LocalDate fechaSiniestro,
        ClaimStatus estado,
        String userEmail,
        LocalDateTime fechaCreacion
) {}

package com.example.preparcialarle.dto.claim;

import com.example.preparcialarle.model.ClaimType;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDate;

public record ClaimRequest(
        @NotNull ClaimType tipoSiniestro,
        @NotBlank String descripcion,
        @NotNull @DecimalMin("1.0") BigDecimal valorEstimado,
        @NotBlank String ubicacion,
        @NotNull LocalDate fechaSiniestro
) {}

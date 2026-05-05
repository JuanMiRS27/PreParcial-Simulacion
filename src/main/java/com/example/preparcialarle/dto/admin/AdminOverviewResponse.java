package com.example.preparcialarle.dto.admin;

public record AdminOverviewResponse(
        long aprobados,
        long denegados,
        long pendientes,
        long requiereRevision
) {}

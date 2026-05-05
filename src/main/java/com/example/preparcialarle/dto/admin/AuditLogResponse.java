package com.example.preparcialarle.dto.admin;

import java.time.LocalDateTime;

public record AuditLogResponse(
        Long id,
        String actorEmail,
        String action,
        String detail,
        LocalDateTime createdAt
) {}

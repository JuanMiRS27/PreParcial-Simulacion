package com.example.preparcialarle.service;

import com.example.preparcialarle.dto.admin.AuditLogResponse;
import com.example.preparcialarle.model.AuditLog;
import com.example.preparcialarle.repository.AuditLogRepository;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class AuditService {
    private final AuditLogRepository auditLogRepository;

    public AuditService(AuditLogRepository auditLogRepository) {
        this.auditLogRepository = auditLogRepository;
    }

    public void log(String actorEmail, String action, String detail) {
        AuditLog log = new AuditLog();
        log.setActorEmail(actorEmail);
        log.setAction(action);
        log.setDetail(detail);
        auditLogRepository.save(log);
    }

    public List<AuditLogResponse> latest() {
        return auditLogRepository.findTop30ByOrderByCreatedAtDesc().stream()
                .map(item -> new AuditLogResponse(item.getId(), item.getActorEmail(), item.getAction(), item.getDetail(), item.getCreatedAt()))
                .toList();
    }
}

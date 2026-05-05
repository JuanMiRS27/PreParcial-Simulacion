package com.example.preparcialarle.service;

import com.example.preparcialarle.dto.evaluation.EvaluationResponse;
import com.example.preparcialarle.exception.NotFoundException;
import com.example.preparcialarle.exception.UnauthorizedException;
import com.example.preparcialarle.model.Claim;
import com.example.preparcialarle.model.ClaimStatus;
import com.example.preparcialarle.model.Evaluation;
import com.example.preparcialarle.model.User;
import com.example.preparcialarle.repository.EvaluationRepository;
import com.example.preparcialarle.repository.UserRepository;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class EvaluationService {
    private final EvaluationRepository evaluationRepository;
    private final ClaimService claimService;
    private final ClaimEvaluationService claimEvaluationService;
    private final UserRepository userRepository;
    private final AuditService auditService;

    public EvaluationService(
            EvaluationRepository evaluationRepository,
            ClaimService claimService,
            ClaimEvaluationService claimEvaluationService,
            UserRepository userRepository,
            AuditService auditService
    ) {
        this.evaluationRepository = evaluationRepository;
        this.claimService = claimService;
        this.claimEvaluationService = claimEvaluationService;
        this.userRepository = userRepository;
        this.auditService = auditService;
    }

    public EvaluationResponse evaluateClaim(Long claimId, String email) {
        Claim claim = claimService.getEntity(claimId);
        validateOwnerOrAdmin(claim, email);

        Evaluation evaluation = evaluationRepository.findByClaim(claim).orElse(new Evaluation());
        evaluation.setClaim(claim);
        int risk = claimEvaluationService.calculateRisk(claim);
        ClaimStatus result = claimEvaluationService.evaluate(claim);
        evaluation.setPuntajeRiesgo(risk);
        evaluation.setMotivo(claimEvaluationService.generateReason(claim, result, risk));
        evaluation.setResultado(result);
        claim.setEstado(result);
        Evaluation saved = evaluationRepository.save(evaluation);
        auditService.log(email, "CLAIM_AUTO_EVALUATED", "Siniestro #" + claimId + " resultado " + result);
        return toResponse(saved);
    }

    public EvaluationResponse adminDecision(Long claimId, String email, boolean approved, String reason) {
        User user = findUser(email);
        if (user.getRole().name().equals("USER")) {
            throw new UnauthorizedException("Solo un admin puede tomar una decision manual");
        }
        Claim claim = claimService.getEntity(claimId);
        Evaluation evaluation = evaluationRepository.findByClaim(claim)
                .orElseThrow(() -> new NotFoundException("El siniestro no tiene evaluacion"));
        if (evaluation.getResultado() != ClaimStatus.REQUIERE_REVISION) {
            throw new IllegalArgumentException("Solo se puede decidir manualmente un siniestro en REQUIERE_REVISION");
        }

        ClaimStatus finalStatus = approved ? ClaimStatus.APROBADO : ClaimStatus.RECHAZADO;
        evaluation.setResultado(finalStatus);
        String extraReason = (reason == null || reason.isBlank()) ? "Decision manual de admin" : reason.trim();
        evaluation.setMotivo(evaluation.getMotivo() + " | " + extraReason);
        claim.setEstado(finalStatus);
        Evaluation saved = evaluationRepository.save(evaluation);
        auditService.log(email, "CLAIM_ADMIN_DECISION", "Siniestro #" + claimId + " " + finalStatus);
        return toResponse(saved);
    }

    public EvaluationResponse getByClaim(Long claimId, String email) {
        Claim claim = claimService.getEntity(claimId);
        validateOwnerOrAdmin(claim, email);
        Evaluation evaluation = evaluationRepository.findByClaim(claim)
                .orElseThrow(() -> new NotFoundException("El siniestro no tiene evaluacion"));
        return toResponse(evaluation);
    }

    public List<EvaluationResponse> listMine(String email) {
        User user = findUser(email);
        return evaluationRepository.findByClaimUser(user).stream().map(this::toResponse).toList();
    }

    public List<EvaluationResponse> listAll() {
        return evaluationRepository.findAll().stream().map(this::toResponse).toList();
    }

    private User findUser(String email) {
        return userRepository.findByEmail(email).orElseThrow(() -> new NotFoundException("Usuario no encontrado"));
    }

    private void validateOwnerOrAdmin(Claim claim, String email) {
        User user = findUser(email);
        boolean isAdmin = "ADMIN".equals(user.getRole().name());
        if (!isAdmin && !claim.getUser().getId().equals(user.getId())) {
            throw new UnauthorizedException("No tienes permiso para esta evaluacion");
        }
    }

    private EvaluationResponse toResponse(Evaluation e) {
        return new EvaluationResponse(
                e.getId(),
                e.getClaim().getId(),
                e.getResultado(),
                e.getPuntajeRiesgo(),
                e.getMotivo(),
                e.getFechaEvaluacion()
        );
    }
}

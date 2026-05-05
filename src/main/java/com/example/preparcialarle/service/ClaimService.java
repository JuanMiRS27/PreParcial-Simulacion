package com.example.preparcialarle.service;

import com.example.preparcialarle.dto.claim.ClaimRequest;
import com.example.preparcialarle.dto.claim.ClaimResponse;
import com.example.preparcialarle.exception.NotFoundException;
import com.example.preparcialarle.exception.UnauthorizedException;
import com.example.preparcialarle.model.Claim;
import com.example.preparcialarle.model.ClaimStatus;
import com.example.preparcialarle.model.Evaluation;
import com.example.preparcialarle.model.User;
import com.example.preparcialarle.repository.ClaimRepository;
import com.example.preparcialarle.repository.EvaluationRepository;
import com.example.preparcialarle.repository.UserRepository;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class ClaimService {
    private final ClaimRepository claimRepository;
    private final UserRepository userRepository;
    private final EvaluationRepository evaluationRepository;
    private final ClaimEvaluationService claimEvaluationService;
    private final AuditService auditService;

    public ClaimService(
            ClaimRepository claimRepository,
            UserRepository userRepository,
            EvaluationRepository evaluationRepository,
            ClaimEvaluationService claimEvaluationService,
            AuditService auditService
    ) {
        this.claimRepository = claimRepository;
        this.userRepository = userRepository;
        this.evaluationRepository = evaluationRepository;
        this.claimEvaluationService = claimEvaluationService;
        this.auditService = auditService;
    }

    public ClaimResponse create(ClaimRequest request, String userEmail) {
        User user = findUser(userEmail);
        Claim claim = new Claim();
        claim.setTipoSiniestro(request.tipoSiniestro());
        claim.setDescripcion(request.descripcion());
        claim.setValorEstimado(request.valorEstimado());
        claim.setUbicacion(request.ubicacion());
        claim.setFechaSiniestro(request.fechaSiniestro());
        claim.setEstado(ClaimStatus.PENDIENTE);
        claim.setUser(user);
        Claim saved = claimRepository.save(claim);
        createOrUpdateAutomaticEvaluation(saved);
        auditService.log(userEmail, "CLAIM_CREATED", "Siniestro #" + saved.getId() + " creado");
        return toResponse(claimRepository.save(saved));
    }

    public List<ClaimResponse> listMine(String email) {
        return claimRepository.findByUser(findUser(email)).stream().map(this::toResponse).toList();
    }

    public List<ClaimResponse> listAll() {
        return claimRepository.findAll().stream().map(this::toResponse).toList();
    }

    public ClaimResponse getById(Long id, String email) {
        Claim claim = getEntity(id);
        validateOwnerOrAdmin(claim, email);
        return toResponse(claim);
    }

    public Claim getEntity(Long id) {
        return claimRepository.findById(id).orElseThrow(() -> new NotFoundException("Siniestro no encontrado"));
    }

    public ClaimResponse update(Long id, ClaimRequest request, String email) {
        Claim claim = getEntity(id);
        validateOwnerOrAdmin(claim, email);
        claim.setTipoSiniestro(request.tipoSiniestro());
        claim.setDescripcion(request.descripcion());
        claim.setValorEstimado(request.valorEstimado());
        claim.setUbicacion(request.ubicacion());
        claim.setFechaSiniestro(request.fechaSiniestro());
        claim.setEstado(ClaimStatus.PENDIENTE);
        createOrUpdateAutomaticEvaluation(claim);
        Claim updated = claimRepository.save(claim);
        auditService.log(email, "CLAIM_UPDATED", "Siniestro #" + updated.getId() + " actualizado");
        return toResponse(updated);
    }

    public void delete(Long id, String email) {
        Claim claim = getEntity(id);
        validateOwnerOrAdmin(claim, email);
        claimRepository.delete(claim);
        auditService.log(email, "CLAIM_DELETED", "Siniestro #" + id + " eliminado");
    }

    private User findUser(String email) {
        return userRepository.findByEmail(email).orElseThrow(() -> new NotFoundException("Usuario no encontrado"));
    }

    private void validateOwnerOrAdmin(Claim claim, String email) {
        User user = findUser(email);
        boolean isAdmin = "ADMIN".equals(user.getRole().name());
        if (!isAdmin && !claim.getUser().getId().equals(user.getId())) {
            throw new UnauthorizedException("No tienes permiso para este siniestro");
        }
    }

    private ClaimResponse toResponse(Claim claim) {
        return new ClaimResponse(
                claim.getId(),
                claim.getTipoSiniestro(),
                claim.getDescripcion(),
                claim.getValorEstimado(),
                claim.getUbicacion(),
                claim.getFechaSiniestro(),
                claim.getEstado(),
                claim.getUser().getEmail(),
                claim.getFechaCreacion()
        );
    }

    private void createOrUpdateAutomaticEvaluation(Claim claim) {
        Evaluation evaluation = evaluationRepository.findByClaim(claim).orElse(new Evaluation());
        evaluation.setClaim(claim);
        int risk = claimEvaluationService.calculateRisk(claim);
        ClaimStatus result = claimEvaluationService.evaluate(claim);
        evaluation.setPuntajeRiesgo(risk);
        evaluation.setResultado(result);
        evaluation.setMotivo(claimEvaluationService.generateReason(claim, result, risk));
        claim.setEstado(result);
        evaluationRepository.save(evaluation);
    }
}

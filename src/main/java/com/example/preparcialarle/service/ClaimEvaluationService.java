package com.example.preparcialarle.service;

import com.example.preparcialarle.dto.admin.EvaluationParametersRequest;
import com.example.preparcialarle.dto.admin.EvaluationParametersResponse;
import com.example.preparcialarle.model.Claim;
import com.example.preparcialarle.model.ClaimStatus;
import com.example.preparcialarle.model.ClaimType;
import com.example.preparcialarle.model.EvaluationParameters;
import com.example.preparcialarle.repository.EvaluationParametersRepository;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class ClaimEvaluationService {
    private static final long PARAMS_ID = 1L;
    private final EvaluationParametersRepository parametersRepository;

    public ClaimEvaluationService(EvaluationParametersRepository parametersRepository) {
        this.parametersRepository = parametersRepository;
    }

    public ClaimStatus evaluate(Claim claim) {
        EvaluationParameters p = getParametersEntity();
        BigDecimal value = claim.getValorEstimado();
        String description = claim.getDescripcion() == null ? "" : claim.getDescripcion().trim();

        if (claim.getTipoSiniestro() == ClaimType.VEHICULO && value.compareTo(BigDecimal.valueOf(p.getVehicleAutoApproveThreshold())) <= 0) {
            return ClaimStatus.APROBADO;
        }
        if (value.compareTo(BigDecimal.valueOf(p.getLowAmountThreshold())) <= 0) return ClaimStatus.APROBADO;
        if (value.compareTo(BigDecimal.valueOf(p.getMediumAmountThreshold())) > 0) return ClaimStatus.RECHAZADO;
        if (claim.getTipoSiniestro() == ClaimType.ROBO && value.compareTo(BigDecimal.valueOf(p.getRobberyReviewThreshold())) > 0) {
            return ClaimStatus.REQUIERE_REVISION;
        }
        if (description.length() < p.getMinDescriptionLength()) return ClaimStatus.REQUIERE_REVISION;
        return ClaimStatus.REQUIERE_REVISION;
    }

    public int calculateRisk(Claim claim) {
        EvaluationParameters p = getParametersEntity();
        int risk = 20;
        BigDecimal value = claim.getValorEstimado();
        if (value.compareTo(BigDecimal.valueOf(p.getLowAmountThreshold())) > 0) risk += 20;
        if (value.compareTo(BigDecimal.valueOf(p.getMediumAmountThreshold())) > 0) risk += 30;
        if (claim.getDescripcion() == null || claim.getDescripcion().trim().length() < p.getMinDescriptionLength()) risk += 20;
        if (claim.getTipoSiniestro() == ClaimType.ROBO) risk += 15;
        if (claim.getTipoSiniestro() == ClaimType.SALUD) risk += 10;
        return Math.min(100, risk);
    }

    public String generateReason(Claim claim, ClaimStatus result, int risk) {
        EvaluationParameters p = getParametersEntity();
        List<String> triggered = new ArrayList<>();
        BigDecimal value = claim.getValorEstimado();
        String description = claim.getDescripcion() == null ? "" : claim.getDescripcion().trim();

        if (description.length() < p.getMinDescriptionLength()) triggered.add("Descripcion muy corta (<" + p.getMinDescriptionLength() + ")");
        if (claim.getTipoSiniestro() == ClaimType.ROBO && value.compareTo(BigDecimal.valueOf(p.getRobberyReviewThreshold())) > 0) {
            triggered.add("Robo > " + p.getRobberyReviewThreshold());
        }
        if (claim.getTipoSiniestro() == ClaimType.VEHICULO && value.compareTo(BigDecimal.valueOf(p.getVehicleAutoApproveThreshold())) <= 0) {
            triggered.add("Vehiculo <= " + p.getVehicleAutoApproveThreshold());
        }
        if (value.compareTo(BigDecimal.valueOf(p.getLowAmountThreshold())) <= 0) triggered.add("Valor <= " + p.getLowAmountThreshold());
        if (value.compareTo(BigDecimal.valueOf(p.getLowAmountThreshold())) > 0 && value.compareTo(BigDecimal.valueOf(p.getMediumAmountThreshold())) <= 0) {
            triggered.add("Valor intermedio");
        }
        if (value.compareTo(BigDecimal.valueOf(p.getMediumAmountThreshold())) > 0) triggered.add("Valor alto");

        String details = triggered.isEmpty() ? "Sin reglas adicionales activadas" : String.join(", ", triggered);
        return "Resultado " + result + ". Riesgo " + risk + "/100. Reglas: " + details;
    }

    public EvaluationParametersResponse getParameters() {
        EvaluationParameters p = getParametersEntity();
        return toResponse(p);
    }

    public EvaluationParametersResponse updateParameters(EvaluationParametersRequest request) {
        if (request.lowAmountThreshold() >= request.mediumAmountThreshold()) {
            throw new IllegalArgumentException("El umbral bajo debe ser menor que el umbral medio");
        }
        EvaluationParameters p = getParametersEntity();
        p.setLowAmountThreshold(request.lowAmountThreshold());
        p.setMediumAmountThreshold(request.mediumAmountThreshold());
        p.setRobberyReviewThreshold(request.robberyReviewThreshold());
        p.setVehicleAutoApproveThreshold(request.vehicleAutoApproveThreshold());
        p.setMinDescriptionLength(request.minDescriptionLength());
        return toResponse(parametersRepository.save(p));
    }

    private EvaluationParameters getParametersEntity() {
        return parametersRepository.findById(PARAMS_ID).orElseGet(() -> {
            EvaluationParameters defaults = new EvaluationParameters();
            defaults.setId(PARAMS_ID);
            defaults.setLowAmountThreshold(1000000);
            defaults.setMediumAmountThreshold(5000000);
            defaults.setRobberyReviewThreshold(3000000);
            defaults.setVehicleAutoApproveThreshold(2000000);
            defaults.setMinDescriptionLength(20);
            return parametersRepository.save(defaults);
        });
    }

    private EvaluationParametersResponse toResponse(EvaluationParameters p) {
        return new EvaluationParametersResponse(
                p.getLowAmountThreshold(),
                p.getMediumAmountThreshold(),
                p.getRobberyReviewThreshold(),
                p.getVehicleAutoApproveThreshold(),
                p.getMinDescriptionLength(),
                p.getUpdatedAt()
        );
    }
}

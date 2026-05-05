package com.example.preparcialarle.controller;

import com.example.preparcialarle.dto.evaluation.AdminDecisionRequest;
import com.example.preparcialarle.dto.evaluation.EvaluationResponse;
import com.example.preparcialarle.service.EvaluationService;
import jakarta.validation.Valid;
import java.security.Principal;
import java.util.List;
import org.springframework.context.annotation.Profile;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/evaluations")
@Profile("claims-eval")
public class EvaluationController {
    private final EvaluationService evaluationService;

    public EvaluationController(EvaluationService evaluationService) {
        this.evaluationService = evaluationService;
    }

    @PostMapping("/claim/{claimId}")
    public EvaluationResponse evaluate(@PathVariable Long claimId, Principal principal) {
        return evaluationService.evaluateClaim(claimId, principal.getName());
    }

    @PutMapping("/claim/{claimId}/decision")
    @PreAuthorize("hasRole('ADMIN')")
    public EvaluationResponse adminDecision(
            @PathVariable Long claimId,
            @Valid @RequestBody AdminDecisionRequest request,
            Principal principal
    ) {
        return evaluationService.adminDecision(claimId, principal.getName(), request.aprobado(), request.motivo());
    }

    @GetMapping("/claim/{claimId}")
    public EvaluationResponse getByClaim(@PathVariable Long claimId, Principal principal) {
        return evaluationService.getByClaim(claimId, principal.getName());
    }

    @GetMapping
    public List<EvaluationResponse> listMine(Principal principal) {
        return evaluationService.listMine(principal.getName());
    }

    @GetMapping("/admin/all")
    @PreAuthorize("hasRole('ADMIN')")
    public List<EvaluationResponse> listAll() {
        return evaluationService.listAll();
    }
}

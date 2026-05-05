package com.example.preparcialarle.controller;

import com.example.preparcialarle.dto.admin.*;
import com.example.preparcialarle.dto.claim.ClaimResponse;
import com.example.preparcialarle.service.AdminService;
import com.example.preparcialarle.service.AuditService;
import com.example.preparcialarle.service.ClaimEvaluationService;
import jakarta.validation.Valid;
import java.security.Principal;
import java.util.List;
import org.springframework.context.annotation.Profile;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin")
@PreAuthorize("hasRole('ADMIN')")
@Profile("auth-admin")
public class AdminController {
    private final AdminService adminService;
    private final ClaimEvaluationService claimEvaluationService;
    private final AuditService auditService;

    public AdminController(AdminService adminService, ClaimEvaluationService claimEvaluationService, AuditService auditService) {
        this.adminService = adminService;
        this.claimEvaluationService = claimEvaluationService;
        this.auditService = auditService;
    }

    @GetMapping("/overview")
    public AdminOverviewResponse overview() {
        return adminService.getOverview();
    }

    @GetMapping("/claims")
    public List<ClaimResponse> claims(
            @RequestParam(required = false) String id,
            @RequestParam(required = false) String tipo,
            @RequestParam(required = false) String valorEstimado,
            @RequestParam(required = false) String estado
    ) {
        return adminService.getClaimsFiltered(id, tipo, valorEstimado, estado);
    }

    @GetMapping("/users")
    public List<AdminUserResponse> users() {
        return adminService.getUsers();
    }

    @PutMapping("/users/{id}/role")
    public AdminUserResponse updateUserRole(
            @PathVariable Long id,
            @Valid @RequestBody UpdateUserRoleRequest request,
            Principal principal
    ) {
        return adminService.updateUserRole(id, request.role(), principal.getName());
    }

    @DeleteMapping("/users/{id}")
    public void deleteUser(@PathVariable Long id, Principal principal) {
        adminService.deleteUser(id, principal.getName());
    }

    @GetMapping("/parameters")
    public EvaluationParametersResponse parameters() {
        return claimEvaluationService.getParameters();
    }

    @PutMapping("/parameters")
    public EvaluationParametersResponse updateParameters(
            @Valid @RequestBody EvaluationParametersRequest request,
            Principal principal
    ) {
        EvaluationParametersResponse response = claimEvaluationService.updateParameters(request);
        auditService.log(principal.getName(), "ADMIN_PARAMETERS_UPDATED", "Parametros de evaluacion actualizados");
        return response;
    }

    @GetMapping("/audit")
    public List<AuditLogResponse> audit() {
        return auditService.latest();
    }
}

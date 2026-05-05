package com.example.preparcialarle.service;

import com.example.preparcialarle.dto.admin.AdminOverviewResponse;
import com.example.preparcialarle.dto.admin.AdminUserResponse;
import com.example.preparcialarle.dto.claim.ClaimResponse;
import com.example.preparcialarle.model.Claim;
import com.example.preparcialarle.model.ClaimStatus;
import com.example.preparcialarle.model.ClaimType;
import com.example.preparcialarle.model.Role;
import com.example.preparcialarle.model.User;
import com.example.preparcialarle.repository.ClaimRepository;
import com.example.preparcialarle.repository.EvaluationRepository;
import com.example.preparcialarle.repository.UserRepository;
import jakarta.transaction.Transactional;
import java.math.BigDecimal;
import java.util.List;
import java.util.Locale;
import org.springframework.stereotype.Service;

@Service
public class AdminService {
    private final ClaimRepository claimRepository;
    private final EvaluationRepository evaluationRepository;
    private final UserRepository userRepository;
    private final AuditService auditService;

    public AdminService(
            ClaimRepository claimRepository,
            EvaluationRepository evaluationRepository,
            UserRepository userRepository,
            AuditService auditService
    ) {
        this.claimRepository = claimRepository;
        this.evaluationRepository = evaluationRepository;
        this.userRepository = userRepository;
        this.auditService = auditService;
    }

    public AdminOverviewResponse getOverview() {
        List<Claim> claims = claimRepository.findAll();
        long approved = claims.stream().filter(c -> c.getEstado() == ClaimStatus.APROBADO).count();
        long denied = claims.stream().filter(c -> c.getEstado() == ClaimStatus.RECHAZADO).count();
        long pending = claims.stream().filter(c -> c.getEstado() == ClaimStatus.PENDIENTE).count();
        long review = claims.stream().filter(c -> c.getEstado() == ClaimStatus.REQUIERE_REVISION).count();
        return new AdminOverviewResponse(approved, denied, pending, review);
    }

    public List<ClaimResponse> getClaimsFiltered(String id, String tipo, String valorEstimado, String estado) {
        return claimRepository.findAll().stream()
                .filter(c -> matchId(c, id))
                .filter(c -> matchTipo(c, tipo))
                .filter(c -> matchValor(c, valorEstimado))
                .filter(c -> matchEstado(c, estado))
                .map(this::toResponse)
                .toList();
    }

    public List<AdminUserResponse> getUsers() {
        return userRepository.findAll().stream()
                .map(u -> new AdminUserResponse(u.getId(), u.getName(), u.getCedula(), u.getEmail(), u.getRole().name()))
                .toList();
    }

    @Transactional
    public AdminUserResponse updateUserRole(Long userId, String role, String actorEmail) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("Usuario no encontrado"));
        Role nextRole;
        try {
            nextRole = Role.valueOf(role.trim().toUpperCase(Locale.ROOT));
        } catch (Exception ex) {
            throw new IllegalArgumentException("Rol invalido");
        }
        if (user.getEmail().equalsIgnoreCase(actorEmail) && nextRole == Role.USER) {
            throw new IllegalArgumentException("No puedes quitarte el rol ADMIN a ti mismo");
        }
        user.setRole(nextRole);
        User saved = userRepository.save(user);
        auditService.log(actorEmail, "ADMIN_USER_ROLE_UPDATED", "Usuario #" + userId + " => " + nextRole);
        return new AdminUserResponse(saved.getId(), saved.getName(), saved.getCedula(), saved.getEmail(), saved.getRole().name());
    }

    @Transactional
    public void deleteUser(Long userId, String actorEmail) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("Usuario no encontrado"));
        if (user.getEmail().equalsIgnoreCase(actorEmail)) {
            throw new IllegalArgumentException("No puedes eliminar tu propio usuario");
        }

        List<Claim> claims = claimRepository.findByUser(user);
        for (Claim claim : claims) {
            evaluationRepository.findByClaim(claim).ifPresent(evaluationRepository::delete);
        }
        claimRepository.deleteAll(claims);
        userRepository.delete(user);
        auditService.log(actorEmail, "ADMIN_USER_DELETED", "Usuario #" + userId + " eliminado");
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

    private boolean matchId(Claim claim, String id) {
        if (id == null || id.isBlank()) return true;
        return claim.getId().toString().contains(id.trim());
    }

    private boolean matchTipo(Claim claim, String tipo) {
        if (tipo == null || tipo.isBlank()) return true;
        return claim.getTipoSiniestro() == ClaimType.valueOf(tipo.trim().toUpperCase(Locale.ROOT));
    }

    private boolean matchValor(Claim claim, String valor) {
        if (valor == null || valor.isBlank()) return true;
        return claim.getValorEstimado().compareTo(new BigDecimal(valor.trim())) == 0;
    }

    private boolean matchEstado(Claim claim, String estado) {
        if (estado == null || estado.isBlank()) return true;
        return claim.getEstado() == ClaimStatus.valueOf(estado.trim().toUpperCase(Locale.ROOT));
    }
}

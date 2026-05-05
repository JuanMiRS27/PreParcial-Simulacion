package com.example.preparcialarle.controller;

import com.example.preparcialarle.dto.claim.ClaimRequest;
import com.example.preparcialarle.dto.claim.ClaimResponse;
import com.example.preparcialarle.service.ClaimService;
import jakarta.validation.Valid;
import java.security.Principal;
import java.util.List;
import org.springframework.context.annotation.Profile;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/claims")
@Profile("claims-eval")
public class ClaimController {
    private final ClaimService claimService;

    public ClaimController(ClaimService claimService) {
        this.claimService = claimService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ClaimResponse create(@Valid @RequestBody ClaimRequest request, Principal principal) {
        return claimService.create(request, principal.getName());
    }

    @GetMapping
    public List<ClaimResponse> listMine(Principal principal) {
        return claimService.listMine(principal.getName());
    }

    @GetMapping("/{id}")
    public ClaimResponse getById(@PathVariable Long id, Principal principal) {
        return claimService.getById(id, principal.getName());
    }

    @PutMapping("/{id}")
    public ClaimResponse update(@PathVariable Long id, @Valid @RequestBody ClaimRequest request, Principal principal) {
        return claimService.update(id, request, principal.getName());
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id, Principal principal) {
        claimService.delete(id, principal.getName());
    }

    @GetMapping("/admin/all")
    @PreAuthorize("hasRole('ADMIN')")
    public List<ClaimResponse> listAll() {
        return claimService.listAll();
    }
}

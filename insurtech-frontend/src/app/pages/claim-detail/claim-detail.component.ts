import { Component, OnDestroy, OnInit } from '@angular/core';
import { NgClass } from '@angular/common';
import { ActivatedRoute, Router, RouterLink } from '@angular/router';
import { Subject, takeUntil } from 'rxjs';
import { AuthService } from '../../core/services/auth.service';
import { ClaimService } from '../../core/services/claim.service';
import { ClaimResponse } from '../../models/claim.model';

@Component({
  selector: 'app-claim-detail',
  standalone: true,
  imports: [RouterLink, NgClass],
  template: `<section class="panel">
    @if (claim) {
      <h3 class="panel-title">Detalle del siniestro #{{ claim.id }}</h3>
      <div class="detail-grid">
        <div class="detail-label">Tipo</div><div>{{ claim.tipoSiniestro }}</div>
        <div class="detail-label">Descripcion</div><div>{{ claim.descripcion }}</div>
        <div class="detail-label">Valor estimado</div><div>COP {{ claim.valorEstimado }}</div>
        <div class="detail-label">Ubicacion</div><div>{{ claim.ubicacion }}</div>
        <div class="detail-label">Fecha</div><div>{{ claim.fechaSiniestro }}</div>
        <div class="detail-label">Estado</div>
        <div>
          <span class="status-chip" [ngClass]="'status-' + claim.estado.toLowerCase()">
            <span class="material-symbols-rounded">{{ statusIcon(claim.estado) }}</span>
            {{ claim.estado.replace('_', ' ') }}
          </span>
        </div>
      </div>
      <div class="toolbar" style="margin-top: 18px;">
        @if (isAdmin && claim.estado === 'REQUIERE_REVISION') {
          <a class="btn btn-accent" [routerLink]="['/dashboard/evaluations', claim.id]">Resolver como admin</a>
        }
        <button class="btn btn-danger" (click)="remove()">Eliminar</button>
        <a class="btn btn-secondary" routerLink="/dashboard/claims">Volver</a>
      </div>
    }
  </section>`
})
export class ClaimDetailComponent implements OnInit, OnDestroy {
  claim?: ClaimResponse;
  claimId = 0;
  isAdmin = false;
  private destroy$ = new Subject<void>();

  constructor(
    private route: ActivatedRoute,
    private claimService: ClaimService,
    private authService: AuthService,
    private router: Router
  ) {}

  ngOnInit(): void {
    this.claimId = Number(this.route.snapshot.paramMap.get('id'));
    this.isAdmin = this.authService.getRole() === 'ADMIN';
    this.claimService.claims$
      .pipe(takeUntil(this.destroy$))
      .subscribe((claims) => {
        const match = claims.find((c) => c.id === this.claimId);
        if (match) this.claim = match;
      });

    this.claimService.loadMine();
    this.claimService.getById(this.claimId)
      .pipe(takeUntil(this.destroy$))
      .subscribe((res) => this.claim = res);
  }

  remove(): void {
    this.claimService.delete(this.claimId).subscribe(() => this.router.navigate(['/dashboard/claims']));
  }

  statusIcon(status: ClaimResponse['estado']): string {
    switch (status) {
      case 'APROBADO':
        return 'check_circle';
      case 'RECHAZADO':
        return 'cancel';
      case 'REQUIERE_REVISION':
        return 'rule';
      default:
        return 'pending';
    }
  }

  ngOnDestroy(): void {
    this.destroy$.next();
    this.destroy$.complete();
  }
}

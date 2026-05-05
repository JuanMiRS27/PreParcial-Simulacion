import { NgClass } from '@angular/common';
import { Component, OnDestroy, OnInit } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { ActivatedRoute, RouterLink } from '@angular/router';
import { Subject, takeUntil } from 'rxjs';
import { AuthService } from '../../core/services/auth.service';
import { EvaluationService } from '../../core/services/evaluation.service';
import { EvaluationResponse } from '../../models/evaluation.model';

@Component({
  selector: 'app-evaluation-result',
  standalone: true,
  imports: [RouterLink, NgClass, FormsModule],
  template: `<section class="panel">
    @if (evaluation) {
      <h3 class="panel-title">Resultado de evaluacion</h3>
      <div class="detail-grid">
        <div class="detail-label">Siniestro</div><div>#{{ evaluation.claimId }}</div>
        <div class="detail-label">Resultado</div>
        <div>
          <span class="status-chip" [ngClass]="'status-' + evaluation.resultado.toLowerCase()">
            <span class="material-symbols-rounded">{{ statusIcon(evaluation.resultado) }}</span>
            {{ evaluation.resultado.replace('_', ' ') }}
          </span>
        </div>
        <div class="detail-label">Puntaje de riesgo</div><div>{{ evaluation.puntajeRiesgo }} / 100</div>
        <div class="detail-label">Motivo</div><div>{{ evaluation.motivo }}</div>
        <div class="detail-label">Fecha</div><div>{{ evaluation.fechaEvaluacion }}</div>
      </div>

      @if (isAdmin && evaluation.resultado === 'REQUIERE_REVISION') {
        <div class="admin-decision-box">
          <h4>Decision manual de admin</h4>
          <textarea [(ngModel)]="decisionReason" placeholder="Motivo de la decision"></textarea>
          <div class="toolbar">
            <button class="btn btn-primary" (click)="decide(true)">Aprobar</button>
            <button class="btn btn-danger" (click)="decide(false)">Rechazar</button>
          </div>
        </div>
      }
    }
    <div class="toolbar" style="margin-top: 18px;">
      <a class="btn btn-secondary" routerLink="/dashboard/claims">Volver a siniestros</a>
    </div>
  </section>`
})
export class EvaluationResultComponent implements OnInit, OnDestroy {
  evaluation?: EvaluationResponse;
  decisionReason = '';
  isAdmin = false;
  private claimId = 0;
  private destroy$ = new Subject<void>();

  constructor(
    private route: ActivatedRoute,
    private evaluationService: EvaluationService,
    private authService: AuthService
  ) {}

  ngOnInit(): void {
    this.claimId = Number(this.route.snapshot.paramMap.get('claimId'));
    this.isAdmin = this.authService.getRole() === 'ADMIN';
    this.evaluationService.evaluations$
      .pipe(takeUntil(this.destroy$))
      .subscribe((items) => {
        const match = items.find((e) => e.claimId === this.claimId);
        if (match) this.evaluation = match;
      });

    this.evaluationService.loadMine();
    this.evaluationService.getByClaim(this.claimId)
      .pipe(takeUntil(this.destroy$))
      .subscribe((res) => this.evaluation = res);
  }

  decide(approved: boolean): void {
    this.evaluationService.adminDecision(this.claimId, approved, this.decisionReason).subscribe((res) => {
      this.evaluation = res;
      this.decisionReason = '';
    });
  }

  statusIcon(status: EvaluationResponse['resultado']): string {
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

import { AsyncPipe, CurrencyPipe, NgClass } from '@angular/common';
import { Component, OnInit } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { BehaviorSubject, Observable } from 'rxjs';
import { AdminService } from '../../core/services/admin.service';
import { EvaluationService } from '../../core/services/evaluation.service';
import { AdminClaim } from '../../models/admin.model';

@Component({
  selector: 'app-admin-claims',
  standalone: true,
  imports: [FormsModule, AsyncPipe, CurrencyPipe, NgClass],
  template: `<section class="panel">
    <h3 class="panel-title">Siniestros (Admin)</h3>
    <div class="filter-grid">
      <input [(ngModel)]="filters.id" placeholder="ID">
      <select [(ngModel)]="filters.tipo">
        <option value="">Tipo</option><option value="VEHICULO">VEHICULO</option><option value="HOGAR">HOGAR</option><option value="SALUD">SALUD</option><option value="ROBO">ROBO</option>
      </select>
      <input [(ngModel)]="filters.valorEstimado" placeholder="Valor estimado exacto">
      <select [(ngModel)]="filters.estado">
        <option value="">Estado</option><option value="PENDIENTE">PENDIENTE</option><option value="APROBADO">APROBADO</option><option value="RECHAZADO">RECHAZADO</option><option value="REQUIERE_REVISION">REQUIERE_REVISION</option>
      </select>
      <button class="btn btn-primary" (click)="search()">Buscar</button>
      <button class="btn btn-secondary" (click)="clear()">Limpiar filtros</button>
    </div>
    @if (claims$ | async; as claims) {
      <div class="claim-list">
        @for (c of claims; track c.id) {
          <article class="claim-item">
            <div class="claim-item-main">
              <div class="claim-head">
                <span class="claim-code">#{{ c.id }} - {{ c.tipoSiniestro }}</span>
                <span class="status-chip" [ngClass]="'status-' + c.estado.toLowerCase()">{{ c.estado }}</span>
              </div>
              <p class="claim-desc">{{ c.descripcion }}</p>
              <div class="claim-meta">
                <span><span class="material-symbols-rounded">payments</span>{{ c.valorEstimado | currency:'COP':'symbol':'1.0-0' }}</span>
                <span><span class="material-symbols-rounded">location_on</span>{{ c.ubicacion }}</span>
                <span><span class="material-symbols-rounded">mail</span>{{ c.userEmail }}</span>
              </div>
            </div>
            <div class="claim-item-actions">
              @if (c.estado === 'REQUIERE_REVISION') {
                <button class="btn btn-accent" (click)="openDecision(c)">Evaluar</button>
              }
            </div>
          </article>
        }
      </div>
    }
  </section>

  @if (selected) {
    <div class="modal-backdrop" (click)="closeDecision()">
      <section class="modal-panel" (click)="$event.stopPropagation()">
        <h3 class="panel-title">Evaluar siniestro #{{ selected.id }}</h3>
        <p class="claim-desc">{{ selected.descripcion }}</p>
        <p class="stats">Tipo: {{ selected.tipoSiniestro }} · Valor: {{ selected.valorEstimado | currency:'COP':'symbol':'1.0-0' }} · Estado: {{ selected.estado }}</p>
        <textarea [(ngModel)]="decisionReason" placeholder="Motivo de la decision"></textarea>
        <div class="toolbar" style="margin-top: 12px; margin-bottom: 0;">
          <button class="btn btn-primary" (click)="decide(true)">Aprobar</button>
          <button class="btn btn-danger" (click)="decide(false)">Denegar</button>
          <button class="btn btn-secondary" (click)="closeDecision()">Cancelar</button>
        </div>
      </section>
    </div>
  }`
})
export class AdminClaimsComponent implements OnInit {
  private claimsSubject = new BehaviorSubject<AdminClaim[]>([]);
  claims$: Observable<AdminClaim[]> = this.claimsSubject.asObservable();
  filters = { id: '', tipo: '', valorEstimado: '', estado: '' };
  selected?: AdminClaim;
  decisionReason = '';

  constructor(private adminService: AdminService, private evaluationService: EvaluationService) {}

  ngOnInit(): void {
    this.search();
  }

  search(): void {
    this.adminService.getClaims(this.filters).subscribe((claims) => this.claimsSubject.next(claims));
  }

  clear(): void {
    this.filters = { id: '', tipo: '', valorEstimado: '', estado: '' };
    this.search();
  }

  openDecision(claim: AdminClaim): void {
    this.selected = claim;
    this.decisionReason = '';
  }

  closeDecision(): void {
    this.selected = undefined;
  }

  decide(approved: boolean): void {
    if (!this.selected) return;
    this.evaluationService.adminDecision(this.selected.id, approved, this.decisionReason).subscribe(() => {
      this.closeDecision();
      this.search();
    });
  }
}

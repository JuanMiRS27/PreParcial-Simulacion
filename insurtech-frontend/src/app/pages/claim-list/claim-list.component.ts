import { AsyncPipe, CurrencyPipe, DatePipe, NgClass } from '@angular/common';
import { Component, ElementRef, HostListener, OnInit } from '@angular/core';
import { RouterLink } from '@angular/router';
import { Observable } from 'rxjs';
import { ClaimService } from '../../core/services/claim.service';
import { ClaimResponse } from '../../models/claim.model';

@Component({
  selector: 'app-claim-list',
  standalone: true,
  imports: [RouterLink, AsyncPipe, CurrencyPipe, DatePipe, NgClass],
  template: `<section class="panel">
    <h3 class="panel-title">Mis siniestros</h3>
    @if (claims$ | async; as claims) {
      <p class="stats">Total registrados: {{ claims.length }}</p>
      <div class="claim-list">
        @for (c of claims; track c.id) {
          <article class="claim-item" [class.expanded]="expandedId === c.id" (click)="toggleExpand(c.id)">
            <div class="claim-item-main">
              <div class="claim-head">
                <span class="claim-code">Siniestro #{{ c.id }}</span>
                <span class="status-chip" [ngClass]="'status-' + c.estado.toLowerCase()">
                  <span class="material-symbols-rounded">{{ statusIcon(c.estado) }}</span>
                  {{ c.estado.replace('_', ' ') }}
                </span>
              </div>
              <div class="claim-meta">
                <span><span class="material-symbols-rounded">category</span>{{ c.tipoSiniestro }}</span>
                <span><span class="material-symbols-rounded">payments</span>{{ c.valorEstimado | currency:'COP':'symbol':'1.0-0' }}</span>
                <span><span class="material-symbols-rounded">event</span>{{ c.fechaSiniestro | date:'yyyy-MM-dd' }}</span>
              </div>
              @if (expandedId === c.id) {
                <div class="expanded-details">
                  <p class="claim-desc">{{ c.descripcion }}</p>
                  <div class="detail-grid">
                    <div class="detail-label">Ubicacion</div><div>{{ c.ubicacion }}</div>
                    <div class="detail-label">Creado por</div><div>{{ c.userEmail }}</div>
                    <div class="detail-label">Fecha creacion</div><div>{{ c.fechaCreacion | date:'yyyy-MM-dd HH:mm' }}</div>
                  </div>
                  <div class="toolbar" style="margin-top: 12px; margin-bottom: 0;">
                    <a class="btn btn-secondary" [routerLink]="['/dashboard/claims', c.id]" (click)="$event.stopPropagation()">Ir a ficha completa</a>
                  </div>
                </div>
              }
            </div>
          </article>
        }
      </div>
    }
  </section>`
})
export class ClaimListComponent implements OnInit {
  claims$!: Observable<ClaimResponse[]>;
  expandedId?: number;

  constructor(private claimService: ClaimService, private host: ElementRef<HTMLElement>) {}

  ngOnInit(): void {
    this.claims$ = this.claimService.claims$;
    this.claimService.loadMine();
  }

  toggleExpand(id: number): void {
    this.expandedId = this.expandedId === id ? undefined : id;
  }

  @HostListener('document:click', ['$event'])
  onDocumentClick(event: MouseEvent): void {
    if (!this.host.nativeElement.contains(event.target as Node)) {
      this.expandedId = undefined;
    }
  }

  statusIcon(status: ClaimResponse['estado']): string {
    switch (status) {
      case 'APROBADO': return 'check_circle';
      case 'RECHAZADO': return 'cancel';
      case 'REQUIERE_REVISION': return 'rule';
      default: return 'pending';
    }
  }
}

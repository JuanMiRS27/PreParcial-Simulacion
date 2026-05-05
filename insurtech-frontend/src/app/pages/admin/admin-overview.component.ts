import { AsyncPipe, DatePipe } from '@angular/common';
import { Component, OnInit } from '@angular/core';
import { Observable } from 'rxjs';
import { AdminService } from '../../core/services/admin.service';
import { AdminOverview, AuditLogItem } from '../../models/admin.model';

@Component({
  selector: 'app-admin-overview',
  standalone: true,
  imports: [AsyncPipe, DatePipe],
  template: `
    @if (overview$ | async; as overview) {
      <div class="kpi-grid">
        <article class="kpi-card"><h4>Aprobados</h4><p>{{ overview.aprobados }}</p></article>
        <article class="kpi-card"><h4>Denegados</h4><p>{{ overview.denegados }}</p></article>
        <article class="kpi-card"><h4>Pendientes</h4><p>{{ overview.pendientes }}</p></article>
        <article class="kpi-card"><h4>Requiere revision</h4><p>{{ overview.requiereRevision }}</p></article>
      </div>
    }
    <section class="panel" style="margin-top: 14px;">
      <h3 class="panel-title">Registro de auditoria</h3>
      @if (audit$ | async; as logs) {
        <div class="audit-list">
          @for (log of logs; track log.id) {
            <article class="audit-item">
              <div><strong>{{ log.action }}</strong> · {{ log.actorEmail }}</div>
              <div class="muted">{{ log.detail }}</div>
              <div class="muted">{{ log.createdAt | date:'yyyy-MM-dd HH:mm:ss' }}</div>
            </article>
          }
        </div>
      }
    </section>
  `
})
export class AdminOverviewComponent implements OnInit {
  overview$!: Observable<AdminOverview>;
  audit$!: Observable<AuditLogItem[]>;

  constructor(private adminService: AdminService) {}

  ngOnInit(): void {
    this.overview$ = this.adminService.getOverview();
    this.audit$ = this.adminService.getAudit();
  }
}

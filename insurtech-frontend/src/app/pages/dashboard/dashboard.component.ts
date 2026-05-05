import { Component } from '@angular/core';
import { RouterLink, RouterOutlet } from '@angular/router';

@Component({
  selector: 'app-dashboard',
  standalone: true,
  imports: [RouterLink, RouterOutlet],
  template: `<section class="panel">
    <h2 class="panel-title">Panel de siniestros</h2>
    <p class="panel-subtitle">Administra reclamaciones y revisa resultados de evaluacion automatica.</p>
    <div class="toolbar">
      <a class="btn btn-secondary" routerLink="/dashboard/claims">Mis siniestros</a>
      <a class="btn btn-primary" routerLink="/dashboard/claims/new">Crear siniestro</a>
    </div>
    <router-outlet />
  </section>`
})
export class DashboardComponent {}

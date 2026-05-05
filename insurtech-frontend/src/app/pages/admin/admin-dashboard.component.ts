import { Component } from '@angular/core';
import { RouterLink, RouterOutlet } from '@angular/router';

@Component({
  selector: 'app-admin-dashboard',
  standalone: true,
  imports: [RouterLink, RouterOutlet],
  template: `<section class="panel">
    <h2 class="panel-title">Panel de administracion</h2>
    <p class="panel-subtitle">Gestion de usuarios, parametros y revision operativa de siniestros.</p>
    <div class="toolbar">
      <a class="btn btn-secondary" routerLink="/admin/overview">Resumen</a>
      <a class="btn btn-secondary" routerLink="/admin/claims">Siniestros</a>
      <a class="btn btn-secondary" routerLink="/admin/parameters">Parametros</a>
      <a class="btn btn-secondary" routerLink="/admin/users">Usuarios</a>
    </div>
    <router-outlet />
  </section>`
})
export class AdminDashboardComponent {}

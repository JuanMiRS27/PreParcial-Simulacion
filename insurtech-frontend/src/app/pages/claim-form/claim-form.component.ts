import { Component } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { Router } from '@angular/router';
import { ClaimService } from '../../core/services/claim.service';

@Component({
  selector: 'app-claim-form',
  standalone: true,
  imports: [FormsModule],
  template: `<section class="panel">
    <h3 class="panel-title">Nuevo siniestro</h3>
    <p class="panel-subtitle">Completa los datos para generar una reclamacion.</p>
    <form class="form-grid" (ngSubmit)="submit()">
      <div>
        <label for="tipo">Tipo de siniestro</label>
        <select id="tipo" [(ngModel)]="tipoSiniestro" name="tipoSiniestro">
          <option>VEHICULO</option>
          <option>HOGAR</option>
          <option>SALUD</option>
          <option>ROBO</option>
        </select>
      </div>
      <div>
        <label for="descripcion">Descripcion</label>
        <textarea id="descripcion" [(ngModel)]="descripcion" name="descripcion" required></textarea>
      </div>
      <div>
        <label for="valor">Valor estimado</label>
        <input id="valor" [(ngModel)]="valorEstimado" name="valorEstimado" type="number" required>
      </div>
      <div>
        <label for="ubicacion">Ubicacion</label>
        <input id="ubicacion" [(ngModel)]="ubicacion" name="ubicacion" required>
      </div>
      <div>
        <label for="fecha">Fecha del siniestro</label>
        <input id="fecha" [(ngModel)]="fechaSiniestro" name="fechaSiniestro" type="date" required>
      </div>
      <button class="btn btn-accent" type="submit">Guardar siniestro</button>
    </form>
  </section>`
})
export class ClaimFormComponent {
  tipoSiniestro: 'VEHICULO' | 'HOGAR' | 'SALUD' | 'ROBO' = 'VEHICULO';
  descripcion = '';
  valorEstimado = 0;
  ubicacion = '';
  fechaSiniestro = '';
  constructor(private claimService: ClaimService, private router: Router) {}
  submit(): void {
    this.claimService.create({
      tipoSiniestro: this.tipoSiniestro,
      descripcion: this.descripcion,
      valorEstimado: this.valorEstimado,
      ubicacion: this.ubicacion,
      fechaSiniestro: this.fechaSiniestro
    }).subscribe(() => this.router.navigate(['/dashboard/claims']));
  }
}

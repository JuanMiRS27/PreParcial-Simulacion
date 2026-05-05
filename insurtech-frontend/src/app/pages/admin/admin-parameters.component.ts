import { Component, OnInit } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { AdminService } from '../../core/services/admin.service';
import { EvaluationParameters } from '../../models/admin.model';

@Component({
  selector: 'app-admin-parameters',
  standalone: true,
  imports: [FormsModule],
  template: `<section class="panel">
    <h3 class="panel-title">Parametros de autoevaluacion</h3>
    <form class="form-grid" (ngSubmit)="save()">
      <div><label>Umbral aprobacion automatica</label><input type="number" [(ngModel)]="form.lowAmountThreshold" name="lowAmountThreshold" required></div>
      <div><label>Umbral revision intermedia</label><input type="number" [(ngModel)]="form.mediumAmountThreshold" name="mediumAmountThreshold" required></div>
      <div><label>Umbral robo para revision</label><input type="number" [(ngModel)]="form.robberyReviewThreshold" name="robberyReviewThreshold" required></div>
      <div><label>Umbral vehiculo para aprobacion</label><input type="number" [(ngModel)]="form.vehicleAutoApproveThreshold" name="vehicleAutoApproveThreshold" required></div>
      <div><label>Minimo de caracteres en descripcion</label><input type="number" [(ngModel)]="form.minDescriptionLength" name="minDescriptionLength" required></div>
      <button class="btn btn-primary" type="submit">Guardar parametros</button>
    </form>
    @if (updatedAt) {
      <p class="stats">Ultima actualizacion: {{ updatedAt }}</p>
    }
  </section>`
})
export class AdminParametersComponent implements OnInit {
  form = {
    lowAmountThreshold: 1000000,
    mediumAmountThreshold: 5000000,
    robberyReviewThreshold: 3000000,
    vehicleAutoApproveThreshold: 2000000,
    minDescriptionLength: 20
  };
  updatedAt = '';

  constructor(private adminService: AdminService) {}

  ngOnInit(): void {
    this.adminService.getParameters().subscribe((p) => this.patch(p));
  }

  save(): void {
    this.adminService.updateParameters(this.form).subscribe((p) => this.patch(p));
  }

  private patch(p: EvaluationParameters): void {
    this.form = {
      lowAmountThreshold: p.lowAmountThreshold,
      mediumAmountThreshold: p.mediumAmountThreshold,
      robberyReviewThreshold: p.robberyReviewThreshold,
      vehicleAutoApproveThreshold: p.vehicleAutoApproveThreshold,
      minDescriptionLength: p.minDescriptionLength
    };
    this.updatedAt = p.updatedAt;
  }
}

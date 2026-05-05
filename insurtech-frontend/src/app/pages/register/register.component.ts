import { Component } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { Router, RouterLink } from '@angular/router';
import { AuthService } from '../../core/services/auth.service';

@Component({
  selector: 'app-register',
  standalone: true,
  imports: [FormsModule, RouterLink],
  template: `<section class="auth-wrap">
    <div class="panel auth-card">
      <h2 class="panel-title">Crear cuenta</h2>
      <p class="panel-subtitle">Registra un usuario para operar siniestros.</p>
      <form class="form-grid" (ngSubmit)="submit()">
        <div>
          <label for="name">Nombre completo</label>
          <input id="name" [(ngModel)]="name" name="name" placeholder="Nombre" required>
        </div>
        <div>
          <label for="email">Correo</label>
          <input id="email" [(ngModel)]="email" name="email" type="email" placeholder="usuario@mail.com" required>
        </div>
        <div>
          <label for="cedula">Cedula</label>
          <input id="cedula" [(ngModel)]="cedula" name="cedula" placeholder="Documento de identidad" required>
        </div>
        <div>
          <label for="password">Contrasena</label>
          <input id="password" [(ngModel)]="password" name="password" type="password" placeholder="Minimo 6 caracteres" required>
        </div>
        <button class="btn btn-accent" type="submit">Registrar cuenta</button>
      </form>
      <p><a class="link-inline" routerLink="/login">Ya tengo cuenta</a></p>
    </div>
  </section>`
})
export class RegisterComponent {
  name = '';
  email = '';
  cedula = '';
  password = '';
  constructor(private authService: AuthService, private router: Router) {}
  submit(): void {
    this.authService.register({ name: this.name, cedula: this.cedula, email: this.email, password: this.password }).subscribe({
      next: (res) => { this.authService.saveSession(res); this.router.navigate(['/dashboard']); },
      error: () => alert('No se pudo registrar')
    });
  }
}

import { Component } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { Router, RouterLink } from '@angular/router';
import { AuthService } from '../../core/services/auth.service';

@Component({
  selector: 'app-login',
  standalone: true,
  imports: [FormsModule, RouterLink],
  template: `<section class="auth-wrap">
    <div class="panel auth-card">
      <h2 class="panel-title">Iniciar sesion</h2>
      <p class="panel-subtitle">Accede para registrar y evaluar siniestros.</p>
      <form class="form-grid" (ngSubmit)="submit()">
        <div>
          <label for="email">Correo</label>
          <input id="email" [(ngModel)]="email" name="email" type="email" placeholder="usuario@mail.com" required>
        </div>
        <div>
          <label for="password">Contrasena</label>
          <input id="password" [(ngModel)]="password" name="password" type="password" placeholder="******" required>
        </div>
        <button class="btn btn-primary" type="submit">Entrar al sistema</button>
      </form>
      <p><a class="link-inline" routerLink="/register">Crear una cuenta nueva</a></p>
    </div>
  </section>`
})
export class LoginComponent {
  email = '';
  password = '';
  constructor(private authService: AuthService, private router: Router) {}
  submit(): void {
    this.authService.login({ email: this.email, password: this.password }).subscribe({
      next: (res) => { this.authService.saveSession(res); this.router.navigate(['/dashboard']); },
      error: () => alert('Credenciales invalidas')
    });
  }
}

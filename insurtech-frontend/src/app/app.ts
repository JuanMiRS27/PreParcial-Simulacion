import { Component } from '@angular/core';
import { NavigationEnd, Router, RouterLink, RouterOutlet } from '@angular/router';
import { AuthService } from './core/services/auth.service';
import { UserProfile } from './models/auth.model';
import { filter } from 'rxjs';

@Component({
  selector: 'app-root',
  imports: [RouterOutlet, RouterLink],
  templateUrl: './app.html',
  styleUrl: './app.css'
})
export class App {
  profile: UserProfile = { name: '', cedula: '', email: '', role: '' };
  showUserMenu = false;
  isDark = false;
  isAdmin = false;
  inAdminRoute = false;

  constructor(private authService: AuthService, private router: Router) {
    this.isDark = localStorage.getItem('theme') === 'dark';
    this.profile = authService.getProfileFromStorage();
    this.isAdmin = this.profile.role === 'ADMIN';
    if (this.authService.isLoggedIn()) {
      this.authService.me().subscribe({
        next: (profile) => {
          this.profile = profile;
          this.isAdmin = profile.role === 'ADMIN';
          localStorage.setItem('name', profile.name);
          localStorage.setItem('cedula', profile.cedula);
          localStorage.setItem('email', profile.email);
          localStorage.setItem('role', profile.role);
        }
      });
    }
    this.inAdminRoute = this.router.url.startsWith('/admin');
    this.router.events.pipe(filter((e) => e instanceof NavigationEnd)).subscribe(() => {
      this.inAdminRoute = this.router.url.startsWith('/admin');
      this.showUserMenu = false;
    });
    this.applyTheme();
  }

  toggleMenu(): void {
    this.showUserMenu = !this.showUserMenu;
  }

  toggleTheme(): void {
    this.isDark = !this.isDark;
    localStorage.setItem('theme', this.isDark ? 'dark' : 'light');
    this.applyTheme();
  }

  logout(): void {
    this.showUserMenu = false;
    this.authService.logout();
  }

  private applyTheme(): void {
    if (this.isDark) {
      document.body.classList.add('dark-mode');
      return;
    }
    document.body.classList.remove('dark-mode');
  }
}

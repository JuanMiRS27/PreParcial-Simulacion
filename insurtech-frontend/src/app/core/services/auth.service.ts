import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Router } from '@angular/router';
import { Observable } from 'rxjs';
import { AuthResponse, UserProfile } from '../../models/auth.model';

@Injectable({ providedIn: 'root' })
export class AuthService {
  private api = 'https://preparcial-simulacion.onrender.com/api/auth';

  constructor(private http: HttpClient, private router: Router) {}

  register(payload: { name: string; cedula: string; email: string; password: string }): Observable<AuthResponse> {
    return this.http.post<AuthResponse>(`${this.api}/register`, payload);
  }

  login(payload: { email: string; password: string }): Observable<AuthResponse> {
    return this.http.post<AuthResponse>(`${this.api}/login`, payload);
  }

  saveSession(auth: AuthResponse): void {
    localStorage.setItem('token', auth.token);
    localStorage.setItem('name', auth.name);
    localStorage.setItem('cedula', auth.cedula);
    localStorage.setItem('email', auth.email);
    localStorage.setItem('role', auth.role);
  }

  me(): Observable<UserProfile> {
    return this.http.get<UserProfile>(`${this.api}/me`);
  }

  getToken(): string | null {
    return localStorage.getItem('token');
  }

  isLoggedIn(): boolean {
    return !!this.getToken();
  }

  logout(): void {
    localStorage.clear();
    this.router.navigate(['/login']);
  }

  getRole(): string {
    return localStorage.getItem('role') ?? '';
  }

  getProfileFromStorage(): UserProfile {
    return {
      name: localStorage.getItem('name') ?? '',
      cedula: localStorage.getItem('cedula') ?? '',
      email: localStorage.getItem('email') ?? '',
      role: localStorage.getItem('role') ?? ''
    };
  }
}

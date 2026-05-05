import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';
import { AdminClaim, AdminClaimFilters, AdminOverview, AdminUser, AuditLogItem, EvaluationParameters } from '../../models/admin.model';

@Injectable({ providedIn: 'root' })
export class AdminService {
  private api = 'http://localhost:8081/api/admin';

  constructor(private http: HttpClient) {}

  getOverview(): Observable<AdminOverview> {
    return this.http.get<AdminOverview>(`${this.api}/overview`);
  }

  getAudit(): Observable<AuditLogItem[]> {
    return this.http.get<AuditLogItem[]>(`${this.api}/audit`);
  }

  getUsers(): Observable<AdminUser[]> {
    return this.http.get<AdminUser[]>(`${this.api}/users`);
  }

  updateUserRole(userId: number, role: 'USER' | 'ADMIN'): Observable<AdminUser> {
    return this.http.put<AdminUser>(`${this.api}/users/${userId}/role`, { role });
  }

  deleteUser(userId: number): Observable<void> {
    return this.http.delete<void>(`${this.api}/users/${userId}`);
  }

  getParameters(): Observable<EvaluationParameters> {
    return this.http.get<EvaluationParameters>(`${this.api}/parameters`);
  }

  updateParameters(payload: Omit<EvaluationParameters, 'updatedAt'>): Observable<EvaluationParameters> {
    return this.http.put<EvaluationParameters>(`${this.api}/parameters`, payload);
  }

  getClaims(filters: AdminClaimFilters): Observable<AdminClaim[]> {
    let params = new HttpParams();
    if (filters.id) params = params.set('id', filters.id);
    if (filters.tipo) params = params.set('tipo', filters.tipo);
    if (filters.valorEstimado) params = params.set('valorEstimado', filters.valorEstimado);
    if (filters.estado) params = params.set('estado', filters.estado);
    return this.http.get<AdminClaim[]>(`${this.api}/claims`, { params });
  }
}

import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { BehaviorSubject, Observable, tap } from 'rxjs';
import { ClaimRequest, ClaimResponse } from '../../models/claim.model';

@Injectable({ providedIn: 'root' })
export class ClaimService {
  private api = 'http://localhost:8082/api/claims';
  private claimsSubject = new BehaviorSubject<ClaimResponse[]>([]);
  claims$ = this.claimsSubject.asObservable();

  constructor(private http: HttpClient) {}

  loadMine(): void {
    this.http.get<ClaimResponse[]>(this.api).subscribe((claims) => this.claimsSubject.next(claims));
  }

  create(payload: ClaimRequest): Observable<ClaimResponse> {
    return this.http.post<ClaimResponse>(this.api, payload).pipe(
      tap((created) => this.claimsSubject.next([created, ...this.claimsSubject.value]))
    );
  }

  getById(id: number): Observable<ClaimResponse> {
    return this.http.get<ClaimResponse>(`${this.api}/${id}`);
  }

  update(id: number, payload: ClaimRequest): Observable<ClaimResponse> {
    return this.http.put<ClaimResponse>(`${this.api}/${id}`, payload).pipe(
      tap((updated) => {
        const next = this.claimsSubject.value.map((c) => c.id === updated.id ? updated : c);
        this.claimsSubject.next(next);
      })
    );
  }

  delete(id: number): Observable<void> {
    return this.http.delete<void>(`${this.api}/${id}`).pipe(
      tap(() => this.claimsSubject.next(this.claimsSubject.value.filter((c) => c.id !== id)))
    );
  }
}

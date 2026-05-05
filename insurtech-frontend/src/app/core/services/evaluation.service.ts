import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { BehaviorSubject, Observable, tap } from 'rxjs';
import { EvaluationResponse } from '../../models/evaluation.model';
import { ClaimService } from './claim.service';

@Injectable({ providedIn: 'root' })
export class EvaluationService {
  private api = 'http://localhost:8082/api/evaluations';
  private evaluationsSubject = new BehaviorSubject<EvaluationResponse[]>([]);
  evaluations$ = this.evaluationsSubject.asObservable();

  constructor(private http: HttpClient, private claimService: ClaimService) {}

  loadMine(): void {
    this.http.get<EvaluationResponse[]>(this.api).subscribe((items) => this.evaluationsSubject.next(items));
  }

  evaluate(claimId: number): Observable<EvaluationResponse> {
    return this.http.post<EvaluationResponse>(`${this.api}/claim/${claimId}`, {}).pipe(
      tap((ev) => {
        const current = this.evaluationsSubject.value.filter((e) => e.claimId !== claimId);
        this.evaluationsSubject.next([ev, ...current]);
        this.claimService.loadMine();
      })
    );
  }

  adminDecision(claimId: number, aprobado: boolean, motivo: string): Observable<EvaluationResponse> {
    return this.http.put<EvaluationResponse>(`${this.api}/claim/${claimId}/decision`, { aprobado, motivo }).pipe(
      tap((ev) => {
        const current = this.evaluationsSubject.value.filter((e) => e.claimId !== claimId);
        this.evaluationsSubject.next([ev, ...current]);
        this.claimService.loadMine();
      })
    );
  }

  getByClaim(claimId: number): Observable<EvaluationResponse> {
    return this.http.get<EvaluationResponse>(`${this.api}/claim/${claimId}`).pipe(
      tap((ev) => {
        const current = this.evaluationsSubject.value.filter((e) => e.claimId !== claimId);
        this.evaluationsSubject.next([ev, ...current]);
      })
    );
  }

  listMine(): Observable<EvaluationResponse[]> {
    return this.http.get<EvaluationResponse[]>(this.api);
  }
}

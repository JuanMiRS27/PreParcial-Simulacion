import { ClaimResponse } from './claim.model';

export interface AdminOverview {
  aprobados: number;
  denegados: number;
  pendientes: number;
  requiereRevision: number;
}

export interface EvaluationParameters {
  lowAmountThreshold: number;
  mediumAmountThreshold: number;
  robberyReviewThreshold: number;
  vehicleAutoApproveThreshold: number;
  minDescriptionLength: number;
  updatedAt: string;
}

export interface AuditLogItem {
  id: number;
  actorEmail: string;
  action: string;
  detail: string;
  createdAt: string;
}

export interface AdminUser {
  id: number;
  name: string;
  cedula: string;
  email: string;
  role: string;
}

export interface AdminClaimFilters {
  id?: string;
  tipo?: string;
  valorEstimado?: string;
  estado?: string;
}

export type AdminClaim = ClaimResponse;

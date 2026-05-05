export interface ClaimRequest {
  tipoSiniestro: 'VEHICULO' | 'HOGAR' | 'SALUD' | 'ROBO';
  descripcion: string;
  valorEstimado: number;
  ubicacion: string;
  fechaSiniestro: string;
}

export interface ClaimResponse extends ClaimRequest {
  id: number;
  estado: 'PENDIENTE' | 'APROBADO' | 'RECHAZADO' | 'REQUIERE_REVISION';
  userEmail: string;
  fechaCreacion: string;
}

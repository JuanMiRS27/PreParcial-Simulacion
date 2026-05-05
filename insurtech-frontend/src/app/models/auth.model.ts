export interface AuthResponse {
  token: string;
  type: string;
  name: string;
  cedula: string;
  email: string;
  role: string;
}

export interface UserProfile {
  name: string;
  cedula: string;
  email: string;
  role: string;
}

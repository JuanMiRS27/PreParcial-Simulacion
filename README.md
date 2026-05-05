# InsurTech - Evaluacion Automatizada de Siniestros

Proyecto academico completo con:
- Backend: Spring Boot + JWT + PostgreSQL
- Frontend: Angular 21
- Docker: `Dockerfile` + `docker-compose.yml`

## Ejecucion local backend (Docker)
```bash
docker compose up --build
```
Servicios backend:
- Auth/Admin: `http://localhost:8081`
- Claims/Evaluations: `http://localhost:8082`

## Ejecucion local frontend (Docker Compose)
```bash
cd insurtech-frontend
docker compose -f docker-compose.frontend.yml up
```
Frontend: `http://localhost:4200`

## Ejecucion backend sin Docker
1. Crear BD `insurtech_db` en PostgreSQL.
2. Configurar variables de entorno usando `.env.example`.
3. Ejecutar:
```bash
./mvnw spring-boot:run
```

## Endpoints principales
- `POST /api/auth/register`
- `POST /api/auth/login`
- `POST /api/claims`
- `GET /api/claims`
- `GET /api/claims/{id}`
- `PUT /api/claims/{id}`
- `DELETE /api/claims/{id}`
- `POST /api/evaluations/claim/{claimId}`
- `GET /api/evaluations/claim/{claimId}`
- `GET /api/evaluations`

## Requests de prueba (Postman/Insomnia)
### Register
`POST /api/auth/register`
```json
{
  "name": "Juan Perez",
  "email": "juan@mail.com",
  "password": "123456"
}
```

### Login
`POST /api/auth/login`
```json
{
  "email": "juan@mail.com",
  "password": "123456"
}
```

### Crear siniestro
`POST /api/claims`
```json
{
  "tipoSiniestro": "VEHICULO",
  "descripcion": "Choque leve en via principal con evidencia fotografica",
  "valorEstimado": 1800000,
  "ubicacion": "Bogota",
  "fechaSiniestro": "2026-05-01"
}
```

### Evaluar siniestro
`POST /api/evaluations/claim/1`

### Consultar evaluacion
`GET /api/evaluations/claim/1`

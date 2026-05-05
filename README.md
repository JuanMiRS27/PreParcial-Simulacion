# InsurTech - Evaluación Automatizada de Siniestros

Proyecto académico completo con:
- Backend: Spring Boot + JWT + PostgreSQL
- Frontend: Angular 21
- Docker: `Dockerfile` + `docker-compose.yml`

## Ejecución local (rápida con Docker)
```bash
docker compose up --build
```
Backend: `http://localhost:8080`

Frontend:
```bash
cd insurtech-frontend
npm install
npm start
```
Frontend: `http://localhost:4200`

## Ejecución backend sin Docker
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
  "ubicacion": "Bogotá",
  "fechaSiniestro": "2026-05-01"
}
```

### Evaluar siniestro
`POST /api/evaluations/claim/1`

### Consultar evaluación
`GET /api/evaluations/claim/1`

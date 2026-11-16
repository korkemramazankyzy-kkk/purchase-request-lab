# Purchase Request Management

A Java 21 and Spring Boot application for managing purchase requests.

## Business purpose

The application validates purchase request status transitions and prevents ordering before a request is approved.

## REST API

Base URL: `/api/v1/purchase-requests`

### Create a purchase request

`POST /api/v1/purchase-requests`

Example request:

```json
{
  "businessKey": "PR-TEST-001",
  "title": "Test purchase request"
}
```

Possible responses:

- `201 Created` — purchase request created. The response includes a `Location` header with the resource URL.
- `400 Bad Request` — validation error.
- `409 Conflict` — duplicate business key.

### Get a purchase request

`GET /api/v1/purchase-requests/{id}`

The `id` parameter is a UUID.

Possible responses:

- `200 OK` — purchase request found.
- `400 Bad Request` — invalid UUID.
- `404 Not Found` — purchase request does not exist.

## Error response

Errors use the following JSON structure:

```json
{
  "code": "VALIDATION",
  "message": "title must not be blank",
  "correlationId": "..."
}
```

Supported error codes include:

- `VALIDATION`
- `NOT_FOUND`
- `DUPLICATE`
- `UNSUPPORTED`
- `INTERNAL`

The application supports the `X-Correlation-Id` request header.

## API documentation

Swagger UI:

`http://localhost:8080/swagger-ui/index.html`

OpenAPI specification:

`http://localhost:8080/v3/api-docs`

## Configuration

The application uses Spring profiles.

- `application.yaml` — shared configuration.
- `application-dev.yaml` — local development configuration.
- `application-prod.yaml` — production database configuration using environment variables.

The downstream service configuration is bound through `DownstreamProperties`.

Development downstream settings:

- Base URL: `http://localhost:9090`
- Timeout: `2s`

No actual downstream network call is performed.

## Health check

Actuator health endpoint:

`http://localhost:8080/actuator/health`

Expected response:

```json
{
  "status": "UP"
}
```

Only the `health` and `info` actuator endpoints are exposed.

## Database

The application uses PostgreSQL and Flyway database migrations.

Development database settings:

- Database: `css`
- Host: `localhost`
- Port: `5432`

## Verification

Run all tests and verify the project.

On Windows PowerShell:

```powershell
.\mvnw.cmd verify
```

Start the application:

```powershell
.\mvnw.cmd spring-boot:run
```

## Test results

The project verification completed successfully:

- Tests run: 23
- Failures: 0
- Errors: 0
- Skipped: 0
- Build: SUCCESS

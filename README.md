
# Purchase Request Management

A Java 21 and Spring Boot application for managing purchase request status transitions.

## Business purpose

The application validates status transitions for purchase requests and prevents ordering before a request is approved.

## Status transition table

| From | To | Allowed |
|---|---|---|
| DRAFT | APPROVED | Yes |
| APPROVED | ORDERED | Yes |
| DRAFT | ORDERED | No |
| RECEIVED | DRAFT | No |

The policy also allows the transition ORDERED -> RECEIVED.

## Package architecture

```text
com.example.purchase
├── config
│   ├── Application
│   └── PurchaseRequestService
├── domain
│   ├── PurchaseRequestId
│   ├── PurchaseRequestStatus
│   ├── PurchaseRequestPolicy
│   ├── Rule
│   ├── RuleChain
│   ├── TransitionRule
│   └── UnapprovedCannotOrderRule
├── dto
├── client
└── handler
```

Dependencies point inward toward the domain.

- `config` starts Spring Boot and wires the rules.
- `PurchaseRequestService` uses the injected `Rule`.
- `domain` contains plain Java business logic and does not import Spring.
- `dto` is reserved for data transfer objects.
- `client` is reserved for outbound integrations.
- `handler` is reserved for request handling.

## Verification

Run the tests and verify the project:

```bash
./mvnw -q verify
```

On Windows PowerShell:

```powershell
.\mvnw.cmd -q verify
```

Start the application:

```powershell
.\mvnw.cmd spring-boot:run
```

## Lab 5 — Spring Security and JWT

### Authentication

The application uses JWT-based authentication with Spring Security.

Login endpoint:

`POST /api/v1/auth/login`

Example request:

```json
{
  "username": "requester",
  "password": "requester-dev"
}
```

Successful response contains:
- `accessToken` — JWT token
- `tokenType` — Bearer
- `expiresIn` — 900 seconds

JWT uses HS256, has a 15-minute lifetime, and includes the issuer, username, and roles.

### Development users

| Username | Password | Role |
|---|---|---|
| requester | requester-dev | ROLE_REQUESTER |
| approver | approver-dev | ROLE_APPROVER |

These accounts are for development only.

### Role permissions

| Action | REQUESTER | APPROVER |
|---|---|---|
| Create purchase request | Allowed | Allowed |
| View purchase request | Allowed | Allowed |
| Approve purchase request | Forbidden (403) | Allowed (200) |

### Protected endpoints

| Endpoint | Access |
|---|---|
| `POST /api/v1/auth/login` | Public |
| `GET /actuator/health` | Public |
| `GET /api/me` | Authenticated users |
| `POST /api/v1/purchase-requests` | REQUESTER, APPROVER |
| `GET /api/v1/purchase-requests/{id}` | REQUESTER, APPROVER |
| `POST /api/v1/purchase-requests/{id}/approve` | APPROVER only |

### Error responses

| Situation | HTTP status | Error code |
|---|---|---|
| Missing or invalid JWT | 401 | UNAUTHENTICATED |
| Invalid username or password | 401 | UNAUTHENTICATED |
| Insufficient role permissions | 403 | FORBIDDEN |

Error response format:

```json
{
  "code": "FORBIDDEN",
  "message": "not allowed for your role",
  "correlationId": "uuid"
}
```

### Verification

The following checks were performed:

- Successful login for REQUESTER and APPROVER.
- JWT authentication for protected endpoints.
- REQUESTER receives 403 when attempting to approve a request.
- APPROVER successfully approves a request.
- Updated APPROVED status is persisted in PostgreSQL.
- `/api/me` returns the authenticated username and authorities.
- `mvn -q verify` completes successfully.


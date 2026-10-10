
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

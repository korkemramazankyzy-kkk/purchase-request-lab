
package com.example.purchase.handler;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/purchase-requests")
public class        PurchaseRequestController {

    private final PurchaseRequestRegistrationService registrationService;

    public PurchaseRequestController(
            PurchaseRequestRegistrationService registrationService
    ) {
        this.registrationService = registrationService;
    }

    @PostMapping
    @Operation(summary = "Create purchase request")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Created"),
            @ApiResponse(responseCode = "400", description = "Validation Error"),
            @ApiResponse(responseCode = "409", description = "Duplicate Business Key")
    })
    public ResponseEntity<PurchaseRequestResponseDto> create(
            @Valid @RequestBody PurchaseRequestCreateDto dto,
            @RequestHeader(value = "X-Correlation-Id", required = false)
            String correlationId
    ) {

        var created = registrationService.register(
                dto.businessKey(),
                dto.title()
        );

        var response = new PurchaseRequestResponseDto(
                created.id(),
                created.businessKey(),
                created.status(),
                created.title()
        );

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(created.id())
                .toUri();

        return ResponseEntity.created(location)
                .header("X-Correlation-Id", getCorrelationId(correlationId))
                .body(response);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get purchase request by UUID")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Found"),
            @ApiResponse(responseCode = "404", description = "Not Found")
    })
    public ResponseEntity<PurchaseRequestResponseDto> getById(
            @PathVariable String id,
            @RequestHeader(value = "X-Correlation-Id", required = false)
            String correlationId
    ) {

        UUID uuid;

        try {
            uuid = UUID.fromString(id);
        } catch (IllegalArgumentException exception) {
            throw new IllegalArgumentException("id must be a UUID");
        }

        var item = registrationService.getById(uuid);

        var response = new PurchaseRequestResponseDto(
                item.id(),
                item.businessKey(),
                item.status(),
                item.title()
        );

        return ResponseEntity.ok()
                .header("X-Correlation-Id", getCorrelationId(correlationId))
                .body(response);
    }

    @PostMapping("/{id}/approve")
    @Operation(summary = "Approve purchase request")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Approved"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "403", description = "Forbidden"),
            @ApiResponse(responseCode = "404", description = "Not Found")
    })
    public ResponseEntity<PurchaseRequestResponseDto> approve(
            @PathVariable UUID id,
            @RequestHeader(value = "X-Correlation-Id", required = false)
            String correlationId
    ) {

        var approved = registrationService.approve(id);

        var response = new PurchaseRequestResponseDto(
                approved.id(),
                approved.businessKey(),
                approved.status(),
                approved.title()
        );

        return ResponseEntity.ok()
                .header("X-Correlation-Id", getCorrelationId(correlationId))
                .body(response);
    }

    private String getCorrelationId(String correlationId) {
        if (correlationId == null || correlationId.isBlank()) {
            return UUID.randomUUID().toString();
        }

        return correlationId;
    }
}

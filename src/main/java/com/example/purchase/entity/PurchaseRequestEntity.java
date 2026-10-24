package com.example.purchase.entity;

import com.example.purchase.domain.PurchaseRequestStatus;
import jakarta.persistence.*;
import java.time.OffsetDateTime;
import java.util.UUID;

@Entity
@Table(name = "purchase_request")
public class PurchaseRequestEntity {

    @Id
    @Column(name = "id", nullable = false)
    private UUID id;

    @Column(name = "business_key", nullable = false,
            unique = true, columnDefinition = "text")
    private String businessKey;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false,
            columnDefinition = "text")
    private PurchaseRequestStatus status;

    @Column(name = "title", nullable = false,
            columnDefinition = "text")
    private String title;

    @Column(name = "created_at", nullable = false,
            insertable = false, updatable = false,
            columnDefinition = "timestamptz")
    private OffsetDateTime createdAt;

    protected PurchaseRequestEntity() {
    }
    public PurchaseRequestEntity(
            UUID id,
            String businessKey,
            PurchaseRequestStatus status,
            String title
    ) {
        this.id = id;
        this.businessKey = businessKey;
        this.status = status;
        this.title = title;
    }

    public UUID getId() {
        return id;
    }

    public String getBusinessKey() {
        return businessKey;
    }

    public PurchaseRequestStatus getStatus() {
        return status;
    }

    public String getTitle() {
        return title;
    }

    public OffsetDateTime getCreatedAt() {
        return createdAt;
    }
}
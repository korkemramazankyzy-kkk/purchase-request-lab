package com.example.purchase.handler;

import java.util.UUID;

public record PurchaseRequestResponseDto(
        UUID id,
        String businessKey,
        String status,
        String title
) {}

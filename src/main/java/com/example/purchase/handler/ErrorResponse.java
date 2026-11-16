package com.example.purchase.handler;

public record ErrorResponse(
        String code,
        String message,
        String correlationId
) {}

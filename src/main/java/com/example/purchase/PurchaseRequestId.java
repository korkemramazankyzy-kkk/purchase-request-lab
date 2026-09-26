package com.example.purchase;

import java.util.Objects;

/**
 * Идентификатор заявки на покупку.
 * Не может быть null или пустой/бланковой строкой.
 */
public record PurchaseRequestId(String value) {

    public PurchaseRequestId {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException("id заявки не может быть null или пустым");
        }
    }
}


package com.example.purchase.domain;

/**
 * Идентификатор заявки на закупку.
 */
public record PurchaseRequestId(String value) {

    public PurchaseRequestId {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException(
                    "id заявки не может быть null или пустым"
            );
        }
    }
}

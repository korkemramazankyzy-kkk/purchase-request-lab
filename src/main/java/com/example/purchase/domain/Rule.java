
package com.example.purchase.domain;

/**
 * Интерфейс бизнес-правила для обработки заявок на закупку.
 */
public interface Rule {

    void check(
            PurchaseRequestId id,
            PurchaseRequestStatus from,
            PurchaseRequestStatus to
    );
}


package com.example.purchase.domain;

/**
 * Правила переходов между статусами заявки на закупку.
 */
public class PurchaseRequestPolicy {

    public PurchaseRequestStatus move(
            PurchaseRequestId id,
            PurchaseRequestStatus from,
            PurchaseRequestStatus to
    ) {
        if (id == null) {
            throw new IllegalArgumentException(
                    "id заявки не может быть null"
            );
        }

        boolean allowed =
                (from == PurchaseRequestStatus.DRAFT
                        && to == PurchaseRequestStatus.APPROVED)
                        || (from == PurchaseRequestStatus.APPROVED
                        && to == PurchaseRequestStatus.ORDERED)
                        || (from == PurchaseRequestStatus.ORDERED
                        && to == PurchaseRequestStatus.RECEIVED);

        if (!allowed) {
            throw new IllegalStateException(
                    "Переход из " + from + " в " + to
                            + " запрещён для заявки " + id.value()
            );
        }

        return to;
    }
}

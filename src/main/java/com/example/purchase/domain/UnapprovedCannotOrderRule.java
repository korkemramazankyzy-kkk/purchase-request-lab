package com.example.purchase.domain;

/**
 * Запрещает размещать заказ без предварительного согласования заявки.
 */
public class UnapprovedCannotOrderRule implements Rule {

    @Override
    public void check(
            PurchaseRequestId id,
            PurchaseRequestStatus from,
            PurchaseRequestStatus to
    ) {
        if (to == PurchaseRequestStatus.ORDERED
                && from != PurchaseRequestStatus.APPROVED) {

            throw new IllegalStateException(
                    "Нельзя разместить заказ без согласования заявки"
            );
        }
    }
}
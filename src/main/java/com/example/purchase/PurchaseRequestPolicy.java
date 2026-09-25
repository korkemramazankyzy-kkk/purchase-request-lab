package com.example.purchase;

import static com.example.purchase.PurchaseRequestStatus.*;

/**
 * Правила переходов между статусами заявки на покупку
 * (аналог TicketPolicy из стартового проекта).
 *
 * Разрешённые переходы:
 *  - DRAFT    -> APPROVED  (согласование пройдено)
 *  - APPROVED -> ORDERED   (заказ размещён у поставщика)
 *
 * Запрещённые переходы (с бизнес-причиной):
 *  - DRAFT    -> ORDERED   (нельзя заказывать без согласования)
 *  - RECEIVED -> DRAFT     (нельзя откатить заявку после получения товара)
 */
public class PurchaseRequestPolicy {

    public PurchaseRequestStatus move(PurchaseRequestId id, PurchaseRequestStatus from, PurchaseRequestStatus to) {
        if (id == null) {
            throw new IllegalArgumentException("id заявки не может быть null");
        }

        boolean allowed =
                (from == DRAFT && to == APPROVED) ||
                (from == APPROVED && to == ORDERED) ||
                (from == ORDERED && to == RECEIVED);

        if (!allowed) {
            throw new IllegalStateException(
                    "Переход из " + from + " в " + to + " запрещён для заявки " + id.value());
        }

        return to;
    }
}

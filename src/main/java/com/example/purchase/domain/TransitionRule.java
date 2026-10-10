
package com.example.purchase.domain;

/**
 * Проверяет допустимость перехода между статусами заявки.
 */
public class TransitionRule implements Rule {

    private final PurchaseRequestPolicy policy;

    public TransitionRule(PurchaseRequestPolicy policy) {
        this.policy = policy;
    }

    @Override
    public void check(
            PurchaseRequestId id,
            PurchaseRequestStatus from,
            PurchaseRequestStatus to
    ) {
        policy.move(id, from, to);
    }
}

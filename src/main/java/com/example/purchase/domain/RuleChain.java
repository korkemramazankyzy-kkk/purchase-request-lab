
package com.example.purchase.domain;

import java.util.List;

/**
 * Выполняет все бизнес-правила по очереди.
 */
public class RuleChain implements Rule {

    private final List<Rule> rules;

    public RuleChain(List<Rule> rules) {
        this.rules = List.copyOf(rules);
    }

    @Override
    public void check(
            PurchaseRequestId id,
            PurchaseRequestStatus from,
            PurchaseRequestStatus to
    ) {
        for (Rule rule : rules) {
            rule.check(id, from, to);
        }
    }
}

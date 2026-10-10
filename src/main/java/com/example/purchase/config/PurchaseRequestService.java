
package com.example.purchase.config;

import com.example.purchase.domain.PurchaseRequestId;
import com.example.purchase.domain.PurchaseRequestStatus;
import com.example.purchase.domain.Rule;
import org.springframework.stereotype.Service;

@Service
public class PurchaseRequestService {

    private final Rule rules;

    public PurchaseRequestService(Rule rules) {
        this.rules = rules;
    }

    public PurchaseRequestStatus move(
            PurchaseRequestId id,
            PurchaseRequestStatus from,
            PurchaseRequestStatus to
    ) {
        rules.check(id, from, to);
        return to;
    }
}

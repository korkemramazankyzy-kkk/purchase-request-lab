
package com.example.purchase.domain;

import java.util.UUID;

public record PurchaseRequest(
        UUID id,
        String businessKey,
        String status,
        String title
) {
}

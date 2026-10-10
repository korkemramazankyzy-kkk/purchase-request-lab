
package com.example.purchase.domain;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static com.example.purchase.domain.PurchaseRequestStatus.*;
import static org.junit.jupiter.api.Assertions.*;

class PurchaseRequestPolicyTest {

    private final PurchaseRequestPolicy policy =
            new PurchaseRequestPolicy();

    private final PurchaseRequestId id =
            new PurchaseRequestId("PR-1001");

    @ParameterizedTest
    @CsvSource({
            "DRAFT, APPROVED, true",
            "APPROVED, ORDERED, true",
            "DRAFT, ORDERED, false",
            "RECEIVED, DRAFT, false"
    })
    void moveFollowsStatusTable(
            PurchaseRequestStatus from,
            PurchaseRequestStatus to,
            boolean allowed
    ) {
        if (allowed) {
            assertEquals(to, policy.move(id, from, to));
        } else {
            assertThrows(
                    IllegalStateException.class,
                    () -> policy.move(id, from, to)
            );
        }
    }

    @Test
    void nullIdThrows() {
        assertThrows(
                IllegalArgumentException.class,
                () -> policy.move(null, DRAFT, APPROVED)
        );
    }

    @Test
    void blankIdThrowsOnConstruction() {
        assertThrows(
                IllegalArgumentException.class,
                () -> new PurchaseRequestId("   ")
        );
    }
}

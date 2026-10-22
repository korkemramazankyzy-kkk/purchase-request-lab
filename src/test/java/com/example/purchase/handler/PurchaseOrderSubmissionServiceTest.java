package com.example.purchase.handler;

import com.example.purchase.client.PurchaseOrderGateway;
import com.example.purchase.domain.PurchaseOrderUnavailable;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

class PurchaseOrderSubmissionServiceTest {

    @Test
    void downstreamFailureBecomesDomainError() {
        PurchaseOrderGateway gateway = mock(PurchaseOrderGateway.class);

        when(gateway.submit("PR-19"))
                .thenThrow(new RuntimeException("503"));

        PurchaseOrderSubmissionService service =
                new PurchaseOrderSubmissionService(gateway);

        assertThrows(
                PurchaseOrderUnavailable.class,
                () -> service.submit("PR-19")
        );

        verify(gateway).submit("PR-19");
        verifyNoMoreInteractions(gateway);
    }
}
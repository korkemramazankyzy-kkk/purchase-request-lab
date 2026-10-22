package com.example.purchase.handler;

import com.example.purchase.client.PurchaseOrderGateway;
import com.example.purchase.domain.PurchaseOrderUnavailable;

public class PurchaseOrderSubmissionService {

    private final PurchaseOrderGateway gateway;

    public PurchaseOrderSubmissionService(PurchaseOrderGateway gateway) {
        this.gateway = gateway;
    }

    public String submit(String businessKey) {
        try {
            return gateway.submit(businessKey);
        } catch (RuntimeException exception) {
            throw new PurchaseOrderUnavailable(businessKey);
        }
    }
}
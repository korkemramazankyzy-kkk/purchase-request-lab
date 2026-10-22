package com.example.purchase.domain;

public class PurchaseOrderUnavailable extends RuntimeException {

    public PurchaseOrderUnavailable(String businessKey) {
        super("Purchase order service is unavailable for: " + businessKey);
    }
}
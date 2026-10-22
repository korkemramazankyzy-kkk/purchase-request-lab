package com.example.purchase.domain;

public class DuplicatePurchaseRequest extends RuntimeException {

    public DuplicatePurchaseRequest(String businessKey) {
        super("Purchase request already exists: " + businessKey);
    }
}
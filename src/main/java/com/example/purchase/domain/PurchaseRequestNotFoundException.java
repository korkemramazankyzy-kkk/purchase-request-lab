package com.example.purchase.domain;

public class PurchaseRequestNotFoundException extends RuntimeException {
    public PurchaseRequestNotFoundException(String message) {
        super(message);
    }
}
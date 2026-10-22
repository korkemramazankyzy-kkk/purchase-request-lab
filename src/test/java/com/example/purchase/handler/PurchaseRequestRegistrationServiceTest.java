package com.example.purchase.handler;

import com.example.purchase.client.PurchaseRequestJdbcRepository;
import com.example.purchase.config.Application;
import com.example.purchase.domain.DuplicatePurchaseRequest;
import com.example.purchase.domain.PurchaseRequestStatus;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataIntegrityViolationException;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest(classes = Application.class)
class PurchaseRequestRegistrationServiceTest {

    @Autowired
    private PurchaseRequestRegistrationService service;

    @Autowired
    private PurchaseRequestJdbcRepository repository;

    @Test
    void insertTwiceRollsBackFirstInsert() {
        String key = "rollback-" + UUID.randomUUID();

        assertThrows(
                DuplicatePurchaseRequest.class,
                () -> service.insertTwice(key)
        );

        assertEquals(0, repository.countByBusinessKey(key));
    }

    @Test
    void duplicateRegistrationKeepsFirstRequest() {
        String key = "register-" + UUID.randomUUID();

        service.register(
                key,
                PurchaseRequestStatus.DRAFT,
                "Laptop"
        );

        assertThrows(
                DuplicatePurchaseRequest.class,
                () -> service.register(
                        key,
                        PurchaseRequestStatus.APPROVED,
                        "Monitor"
                )
        );

        assertEquals(1, repository.countByBusinessKey(key));
    }
}
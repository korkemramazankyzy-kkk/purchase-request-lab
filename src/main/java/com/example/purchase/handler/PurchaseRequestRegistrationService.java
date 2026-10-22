package com.example.purchase.handler;

import com.example.purchase.client.PurchaseRequestJdbcRepository;
import com.example.purchase.domain.DuplicatePurchaseRequest;
import com.example.purchase.domain.PurchaseRequestStatus;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class PurchaseRequestRegistrationService {

    private final PurchaseRequestJdbcRepository repository;

    public PurchaseRequestRegistrationService(
            PurchaseRequestJdbcRepository repository
    ) {
        this.repository = repository;
    }

    @Transactional
    public void insertTwice(String businessKey) {
        try {
            repository.insert(
                    businessKey,
                    PurchaseRequestStatus.DRAFT,
                    "Test purchase request"
            );

            repository.insert(
                    businessKey,
                    PurchaseRequestStatus.DRAFT,
                    "Test purchase request"
            );
        } catch (DataIntegrityViolationException exception) {
            throw new DuplicatePurchaseRequest(businessKey);
        }
    }

    @Transactional
    public void register(
            String businessKey,
            PurchaseRequestStatus status,
            String title
    ) {
        if (repository.countByBusinessKey(businessKey) > 0) {
            throw new DuplicatePurchaseRequest(businessKey);
        }

        try {
            repository.insert(businessKey, status, title);
        } catch (DataIntegrityViolationException exception) {
            throw new DuplicatePurchaseRequest(businessKey);
        }
    }
}
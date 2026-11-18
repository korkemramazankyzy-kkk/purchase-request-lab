
package com.example.purchase.handler;

import com.example.purchase.PurchaseRequestStatus;
import com.example.purchase.client.PurchaseRequestJdbcRepository;
import com.example.purchase.domain.DuplicatePurchaseRequest;
import com.example.purchase.domain.PurchaseRequest;
import com.example.purchase.domain.PurchaseRequestNotFoundException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

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
    public PurchaseRequest register(String businessKey, String title) {
        if (repository.countByBusinessKey(businessKey) > 0) {
            throw new DuplicatePurchaseRequest(businessKey);
        }

        try {
            UUID id = repository.insert(
                    businessKey,
                    PurchaseRequestStatus.DRAFT,
                    title
            );

            return new PurchaseRequest(
                    id,
                    businessKey,
                    PurchaseRequestStatus.DRAFT.name(),
                    title
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

    @Transactional(readOnly = true)
    public PurchaseRequest getById(UUID id) {
        return repository.findById(id)
                .orElseThrow(() ->
                        new PurchaseRequestNotFoundException(
                                "Request with id " + id + " not found"
                        )
                );
    }

    @Transactional
    @PreAuthorize("hasRole('APPROVER')")
    public PurchaseRequest approve(UUID id) {
        PurchaseRequest request = getById(id);

        int updated = repository.updateStatus(
                id,
                PurchaseRequestStatus.APPROVED
        );

        if (updated == 0) {
            throw new PurchaseRequestNotFoundException(
                    "Request with id " + id + " not found"
            );
        }

        return new PurchaseRequest(
                request.id(),
                request.businessKey(),
                PurchaseRequestStatus.APPROVED.name(),
                request.title()
        );
    }
}

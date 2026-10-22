package com.example.purchase.client;

import com.example.purchase.domain.PurchaseRequestStatus;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public class PurchaseRequestJdbcRepository {

    private final JdbcTemplate jdbcTemplate;

    public PurchaseRequestJdbcRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public void insert(
            String businessKey,
            PurchaseRequestStatus status,
            String title
    ) {
        String sql = """
                INSERT INTO purchase_request
                    (id, business_key, status, title)
                VALUES (?, ?, ?, ?)
                """;

        jdbcTemplate.update(
                sql,
                UUID.randomUUID(),
                businessKey,
                status.name(),
                title
        );
    }

    public int countByBusinessKey(String businessKey) {
        String sql = """
                SELECT COUNT(*)
                FROM purchase_request
                WHERE business_key = ?
                """;

        Integer count = jdbcTemplate.queryForObject(
                sql,
                Integer.class,
                businessKey
        );

        return count == null ? 0 : count;
    }
}

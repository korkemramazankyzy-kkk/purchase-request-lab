
package com.example.purchase.client;

import com.example.purchase.PurchaseRequestStatus;
import com.example.purchase.domain.PurchaseRequest;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public class PurchaseRequestJdbcRepository {

    private final JdbcTemplate jdbcTemplate;

    public PurchaseRequestJdbcRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public UUID insert(
            String businessKey,
            PurchaseRequestStatus status,
            String title
    ) {
        UUID id = UUID.randomUUID();

        String sql = """
                INSERT INTO purchase_request
                    (id, business_key, status, title)
                VALUES (?, ?, ?, ?)
                """;

        jdbcTemplate.update(
                sql,
                id,
                businessKey,
                status.name(),
                title
        );

        return id;
    }

    public Optional<PurchaseRequest> findById(UUID id) {
        String sql = """
                SELECT id, business_key, status, title
                FROM purchase_request
                WHERE id = ?
                """;

        try {
            PurchaseRequest request = jdbcTemplate.queryForObject(
                    sql,
                    (rs, rowNum) -> new PurchaseRequest(
                            UUID.fromString(rs.getString("id")),
                            rs.getString("business_key"),
                            rs.getString("status"),
                            rs.getString("title")
                    ),
                    id
            );

            return Optional.ofNullable(request);

        } catch (EmptyResultDataAccessException exception) {
            return Optional.empty();
        }
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

    public int updateStatus(UUID id, PurchaseRequestStatus status) {
        String sql = """
                UPDATE purchase_request
                SET status = ?
                WHERE id = ?
                """;

        return jdbcTemplate.update(
                sql,
                status.name(),
                id
        );
    }
}

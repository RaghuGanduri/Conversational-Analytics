package com.hotbutton.analytics.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hotbutton.analytics.config.AnalyticsProperties;
import com.hotbutton.analytics.dto.SqlValidationResult;
import com.hotbutton.analytics.exception.AnalyticsException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class SqlValidationServiceTest {

    private SqlValidationService validationService;

    @BeforeEach
    void setUp() throws Exception {
        SemanticContextLoader semanticContextLoader = new SemanticContextLoader(new ObjectMapper());
        semanticContextLoader.loadMetadata();
        AnalyticsProperties properties = new AnalyticsProperties();
        validationService = new SqlValidationService(semanticContextLoader, properties);
    }

    @Test
    void acceptsSelectAndAppendsLimit() {
        SqlValidationResult validated = validationService.validate(
                "SELECT severity, COUNT(*) FROM part_claim GROUP BY severity");
        assertThat(validated.sql()).endsWith("LIMIT 5000");
    }

    @Test
    void acceptsSelectStarFromAllowedTable() {
        SqlValidationResult validated = validationService.validate("SELECT * FROM part_claim");
        assertThat(validated.sql()).endsWith("LIMIT 5000");
    }

    @Test
    void acceptsAliasInOrderBy() {
        SqlValidationResult validated = validationService.validate(
                "SELECT severity, COUNT(*) AS claim_count FROM part_claim GROUP BY severity ORDER BY claim_count DESC");
        assertThat(validated.sql()).endsWith("LIMIT 5000");
    }

    @Test
    void rejectsUnsafeInsertStatement() {
        assertThatThrownBy(() -> validationService.validate("INSERT INTO part_claim (claim_id) VALUES (1)"))
                .isInstanceOf(AnalyticsException.class)
                .hasMessageContaining("Forbidden SQL operation");
    }

    @Test
    void rejectsUnsafeDropStatement() {
        assertThatThrownBy(() -> validationService.validate("DROP TABLE part_claim"))
                .isInstanceOf(AnalyticsException.class)
                .hasMessageContaining("Forbidden SQL operation");
    }

    @Test
    void rejectsUnsafeAlterStatement() {
        assertThatThrownBy(() -> validationService.validate("ALTER TABLE part_claim ADD COLUMN new_col varchar(10)"))
                .isInstanceOf(AnalyticsException.class)
                .hasMessageContaining("Forbidden SQL operation");
    }

    @Test
    void rejectsUnauthorizedColumns() {
        assertThatThrownBy(() -> validationService.validate("SELECT secret_value FROM part_claim LIMIT 10"))
                .isInstanceOf(AnalyticsException.class)
                .hasMessageContaining("Unauthorized column");
    }

    @Test
    void rejectsUnauthorizedTableWithJoin() {
        assertThatThrownBy(() -> validationService.validate(
                        "SELECT p.severity FROM part_claim p JOIN secret_table s ON p.claim_id = s.claim_id LIMIT 10"))
                .isInstanceOf(AnalyticsException.class)
                .hasMessageContaining("Unauthorized table");
    }
}

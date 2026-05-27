package com.hotbutton.analytics.service;

import com.hotbutton.analytics.dto.QueryResult;
import com.hotbutton.analytics.dto.SqlValidationResult;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@ActiveProfiles("test")
class QueryExecutionServiceTest {

    @Autowired
    private QueryExecutionService queryExecutionService;

    @Test
    void executesValidatedSelectQuery() {
        SqlValidationResult validatedSql = new SqlValidationResult("SELECT claim_id, part_number, severity FROM part_claim ORDER BY claim_id LIMIT 3");

        QueryResult result = queryExecutionService.execute(validatedSql);

        assertThat(result.columns()).containsExactly("claim_id", "part_number", "severity");
        assertThat(result.rows()).hasSize(3);
        assertThat(result.rowCount()).isEqualTo(3);
        assertThat(result.executionTime()).isGreaterThanOrEqualTo(0);
        assertThat(result.rows().get(0)).containsExactly(1L, "PN-100", "Critical");
    }
}

package com.hotbutton.analytics.service;

import java.util.List;
import org.junit.jupiter.api.Test;

import com.hotbutton.analytics.dto.QueryResult;

import static org.assertj.core.api.Assertions.assertThat;

class InsightGenerationServiceTest {

    private final InsightGenerationService service = new InsightGenerationService();

    @Test
    void returnsNoDataMessageWhenNoRows() {
        QueryResult result = new QueryResult(List.of("claim_id"), List.of(), 0L, 0);

        String summary = service.generate(result);

        assertThat(summary).contains("no data");
    }

    @Test
    void generatesTrendInsightForTimeSeries() {
        QueryResult result = new QueryResult(
                List.of("date", "claim_cost"),
                List.of(
                        List.of("2026-05-20", 1000.0),
                        List.of("2026-05-21", 1200.0)
                ),
                5L,
                2);

        String summary = service.generate(result);

        assertThat(summary).containsIgnoringCase("increased").contains("from 1000.00 to 1200.00");
    }

    @Test
    void generatesCategorySummaryForTwoColumns() {
        QueryResult result = new QueryResult(
                List.of("severity", "claim_count"),
                List.of(
                        List.of("Critical", 20.0),
                        List.of("High", 10.0)
                ),
                5L,
                2);

        String summary = service.generate(result);

        assertThat(summary).containsIgnoringCase("claim count is highest for Critical");
    }

    @Test
    void generatesDefaultSummaryForMultiColumnResult() {
        QueryResult result = new QueryResult(
                List.of("claim_id", "severity", "oem_name"),
                List.of(
                        List.of(1L, "Critical", "Honda"),
                        List.of(2L, "High", "Ford")
                ),
                5L,
                2);

        String summary = service.generate(result);

        assertThat(summary).containsIgnoringCase("returned 2 rows across 3 columns");
    }
}

package com.hotbutton.analytics.service;

import com.hotbutton.analytics.dto.ChartMetadata;
import com.hotbutton.analytics.dto.QueryResult;

import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class ChartMetadataServiceTest {

    private final ChartMetadataService service = new ChartMetadataService();

    @Test
    void buildsBarChartForTwoColumnNumericResult() {
        QueryResult result = new QueryResult(
                List.of("severity", "claim_count"),
                List.of(
                        List.of("Critical", 5L),
                        List.of("High", 8L),
                        List.of("Medium", 3L)
                ),
                0L,
                3);

        ChartMetadata metadata = service.buildChartMetadata(result);

        assertThat(metadata.chartType()).isEqualTo("bar");
        assertThat(metadata.labels()).containsExactly("Critical", "High", "Medium");
        assertThat(metadata.values()).containsExactly(
                List.of(5L),
                List.of(8L),
                List.of(3L)
        );
    }

    @Test
    void buildsPieChartForSmallCategoryResult() {
        QueryResult result = new QueryResult(
                List.of("oem_name", "total_cost"),
                List.of(
                        List.of("Honda", 15000.0),
                        List.of("Ford", 9000.0)
                ),
                0L,
                2);

        ChartMetadata metadata = service.buildChartMetadata(result);

        assertThat(metadata.chartType()).isEqualTo("pie");
        assertThat(metadata.labels()).containsExactly("Honda", "Ford");
        assertThat(metadata.values()).containsExactly(
                List.of(15000.0),
                List.of(9000.0)
        );
    }

    @Test
    void buildsLineChartForTimeSeriesResult() {
        QueryResult result = new QueryResult(
                List.of("bucket_date", "claim_cost"),
                List.of(
                        List.of("2026-05-20", 12000.0),
                        List.of("2026-05-21", 14000.0)
                ),
                0L,
                2);

        ChartMetadata metadata = service.buildChartMetadata(result);

        assertThat(metadata.chartType()).isEqualTo("line");
        assertThat(metadata.labels()).containsExactly("2026-05-20", "2026-05-21");
        assertThat(metadata.values()).containsExactly(
                List.of(12000.0),
                List.of(14000.0)
        );
    }

    @Test
    void buildsTableForMultiColumnResult() {
        QueryResult result = new QueryResult(
                List.of("claim_id", "severity", "oem_name"),
                List.of(
                        List.of(1L, "Critical", "Honda"),
                        List.of(2L, "High", "Ford")
                ),
                0L,
                2);

        ChartMetadata metadata = service.buildChartMetadata(result);

        assertThat(metadata.chartType()).isEqualTo("table");
        assertThat(metadata.labels()).containsExactly("claim_id", "severity", "oem_name");
        assertThat(metadata.values()).containsExactly(
                List.of(1L, "Critical", "Honda"),
                List.of(2L, "High", "Ford")
        );
    }
}

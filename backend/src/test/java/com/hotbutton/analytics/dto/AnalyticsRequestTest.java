package com.hotbutton.analytics.dto;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

class AnalyticsRequestTest {

    @Test
    void builderSetsFieldsAndDefaults() {
        AnalyticsRequest request = AnalyticsRequest.builder()
                .prompt("Show claims by severity")
                .preferredChartType("bar")
                .maxRows(1000)
                .includeGeneratedSql(true)
                .debugMode(true)
                .build();

        assertThat(request.getPrompt()).isEqualTo("Show claims by severity");
        assertThat(request.getPreferredChartType()).isEqualTo("bar");
        assertThat(request.getMaxRows()).isEqualTo(1000);
        assertThat(request.getIncludeGeneratedSql()).isTrue();
        assertThat(request.getIncludeSummary()).isTrue();
        assertThat(request.getDebugMode()).isTrue();
    }
}

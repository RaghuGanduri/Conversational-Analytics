package com.hotbutton.analytics.service;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

class AggregationResolverTest {

    private final AggregationResolver resolver = new AggregationResolver();

    @Test
    void buildsCountStar() {
        String sel = resolver.buildAggregationSelect("count", "*");
        assertThat(sel).isEqualTo("COUNT(*) AS metric_value");
    }

    @Test
    void buildsCountColumn() {
        String sel = resolver.buildAggregationSelect("count", "claim_id");
        assertThat(sel).isEqualTo("COUNT(claim_id) AS metric_value");
    }

    @Test
    void buildsSum() {
        String sel = resolver.buildAggregationSelect("sum", "claim_cost");
        assertThat(sel).isEqualTo("SUM(claim_cost) AS metric_value");
    }

    @Test
    void buildsAvg() {
        String sel = resolver.buildAggregationSelect("avg", "claim_cost");
        assertThat(sel).isEqualTo("AVG(claim_cost) AS metric_value");
    }
}

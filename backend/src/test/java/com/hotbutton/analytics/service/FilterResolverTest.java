package com.hotbutton.analytics.service;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.Test;

class FilterResolverTest {

    private final FilterResolver resolver = new FilterResolver();

    @Test
    void resolvesSingleValueFilter() {
        Map<String, List<String>> filters = Map.of("oem_name", List.of("Honda"));
        String where = resolver.buildWhereClause(filters);
        assertThat(where).isEqualTo(" WHERE oem_name = 'Honda'");
    }

    @Test
    void resolvesMultiValueFilter() {
        Map<String, List<String>> filters = Map.of("oem_name", List.of("Honda", "Ford"));
        String where = resolver.buildWhereClause(filters);
        assertThat(where).isEqualTo(" WHERE oem_name IN ('Honda', 'Ford')");
    }

    @Test
    void resolvesDateRangeFilters() {
        Map<String, List<String>> filters = Map.of("claim_date_from", List.of("2023-01-01"), "claim_date_to", List.of("2023-12-31"));
        String where = resolver.buildWhereClause(filters);
        // order of conditions is stable since Map.of preserves insertion order in Java 9+
        assertThat(where).isEqualTo(" WHERE claim_date >= '2023-01-01' AND claim_date <= '2023-12-31'");
    }

    @Test
    void resolvesBooleanFilterWithoutQuotes() {
        Map<String, List<String>> filters = Map.of("sla_breached_flag", List.of("true"));
        String where = resolver.buildWhereClause(filters);
        assertThat(where).isEqualTo(" WHERE sla_breached_flag = true");
    }
}

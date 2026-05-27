package com.hotbutton.analytics.service;

import com.hotbutton.analytics.dto.SemanticQuery;

import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class SqlBuilderService {

    private final FilterResolver filterResolver;

    private final AggregationResolver aggregationResolver;

    public SqlBuilderService(
            FilterResolver filterResolver,
            AggregationResolver aggregationResolver) {

        this.filterResolver = filterResolver;
        this.aggregationResolver = aggregationResolver;
    }

    public String buildSql(SemanticQuery semanticQuery) {

        if (semanticQuery == null) {

            throw new IllegalArgumentException(
                    "SemanticQuery cannot be null");
        }

        String tableName =
                resolvePhysicalTable(
                        semanticQuery.getDomain());

        StringBuilder sql =
                new StringBuilder("SELECT ");

        String selectClause =
                buildSelectClause(semanticQuery);

        if (selectClause.isBlank()) {

            throw new RuntimeException(
                    "Generated empty SELECT clause");
        }

        sql.append(selectClause);

        sql.append(" FROM ").append(tableName);

        String whereClause =
                filterResolver.buildWhereClause(
                        semanticQuery.getFilters());

        if (!whereClause.isBlank()) {
            sql.append(" ").append(whereClause);
        }

        boolean hasMetric =
                semanticQuery.getMetric() != null
                        && !semanticQuery.getMetric().isBlank();

        if (hasMetric
                && !semanticQuery.getDimensions().isEmpty()) {

            sql.append(" GROUP BY ")
                    .append(
                            String.join(
                                    ", ",
                                    semanticQuery.getDimensions()));
        }

        sql.append(buildOrderByClause(semanticQuery));

        sql.append(" LIMIT ")
                .append(semanticQuery.getLimit());

        return sql.toString();
    }

    private String buildSelectClause(
            SemanticQuery semanticQuery) {

        boolean hasMetric =
                semanticQuery.getMetric() != null
                        && !semanticQuery.getMetric().isBlank();

        List<String> selectParts =
                new ArrayList<>();

        if (hasMetric) {

            if (semanticQuery.getDimensions() != null) {

                selectParts.addAll(
                        semanticQuery.getDimensions());
            }

            String aggregation =
                    aggregationResolver.buildAggregationSelect(
                            semanticQuery.getMetric(),
                            semanticQuery.getMetricColumn());

            if (aggregation != null
                    && !aggregation.isBlank()) {

                selectParts.add(aggregation);
            }

        } else {

            if (semanticQuery.getSelectColumns() != null) {

                selectParts.addAll(
                        semanticQuery.getSelectColumns());
            }
        }

        return String.join(", ", selectParts);
    }

    private String buildOrderByClause(
            SemanticQuery semanticQuery) {

        boolean hasMetric =
                semanticQuery.getMetric() != null
                        && !semanticQuery.getMetric().isBlank();

        if (hasMetric) {
            return " ORDER BY metric_value DESC";
        }

        if (semanticQuery.getSelectColumns() != null
                && !semanticQuery.getSelectColumns().isEmpty()) {

            return " ORDER BY "
                    + semanticQuery.getSelectColumns().get(0);
        }

        return "";
    }

    private String resolvePhysicalTable(String domain) {

        if (domain == null || domain.isBlank()) {
            return "part_claim";
        }

        return switch (domain.toLowerCase()) {

            case "claims" ->
                    "part_claim";

            case "escalations" ->
                    "claim_escalation";

            default ->
                    throw new IllegalArgumentException(
                            "Unsupported domain: " + domain);
        };
    }
}
package com.hotbutton.analytics.service;

import org.springframework.stereotype.Service;

@Service
public class AggregationResolver {

    public String buildAggregationSelect(
            String metric,
            String metricColumn) {

        if (metric == null || metric.isBlank()) {
            return "";
        }

        String normalized =
                metric.toLowerCase().trim();

        return switch (normalized) {

            case "count" ->
                    "COUNT(*) AS metric_value";

            case "sum" ->
                    "SUM(" + metricColumn + ") AS metric_value";

            case "avg" ->
                    "AVG(" + metricColumn + ") AS metric_value";

            case "max" ->
                    "MAX(" + metricColumn + ") AS metric_value";

            case "min" ->
                    "MIN(" + metricColumn + ") AS metric_value";

            default ->
                    throw new IllegalArgumentException(
                            "Unsupported metric: " + metric);
        };
    }
}
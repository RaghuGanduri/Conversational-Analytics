package com.hotbutton.analytics.service;

import java.util.Locale;
import java.sql.Date;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.regex.Pattern;

import org.springframework.stereotype.Service;

import com.hotbutton.analytics.dto.QueryResult;

@Service
public class InsightGenerationService {

    private static final Pattern DATE_PATTERN =
            Pattern.compile("\\d{4}-\\d{2}-\\d{2}");

    private static final Pattern DATETIME_PATTERN =
            Pattern.compile("\\d{4}-\\d{2}-\\d{2}[ T]\\d{2}:\\d{2}(:\\d{2})?");

    public String generate(QueryResult result) {

        if (result == null
                || result.getRows() == null
                || result.getRows().isEmpty()) {

            return "The query returned no data.";
        }

        if (result.getColumns() == null
                || result.getColumns().isEmpty()) {

            return String.format(
                    "The query returned %d rows but column metadata is unavailable.",
                    result.getRowCount());
        }

        String summary = buildSummary(result);

        return String.format(
                "%s The query returned %d rows in %d ms.",
                summary,
                result.getRowCount(),
                result.getExecutionTimeMs());
    }

    private String buildSummary(QueryResult result) {

        int metricIndex = findMetricColumn(result);

        boolean hasMetric = metricIndex >= 0;

        boolean isTimeSeries =
                hasMetric && looksLikeTimeSeries(result);

        if (isTimeSeries) {
            return buildTimeSeriesSummary(result, metricIndex);
        }

        if (hasMetric && result.getColumns().size() == 2) {
            return buildCategorySummary(result, metricIndex);
        }

        if (hasMetric && result.getColumns().size() == 1) {
            return buildMetricSummary(result, metricIndex);
        }

        return buildDefaultSummary(result);
    }

    private String buildTimeSeriesSummary(
            QueryResult result,
            int metricIndex) {

        List<List<Object>> rows = result.getRows();

        Object firstLabel = rows.get(0).get(0);

        Object lastLabel =
                rows.get(rows.size() - 1).get(0);

        double firstValue =
                toDouble(rows.get(0).get(metricIndex));

        double lastValue =
                toDouble(rows.get(rows.size() - 1).get(metricIndex));

        double delta = lastValue - firstValue;

        String change =
                delta > 0
                        ? "increased"
                        : delta < 0
                        ? "decreased"
                        : "remained stable";

        String metricLabel =
                describeMetric(result.getColumns(), metricIndex);

        return String.format(
                "%s %s from %.2f to %.2f between %s and %s.",
                capitalize(metricLabel),
                change,
                firstValue,
                lastValue,
                String.valueOf(firstLabel),
                String.valueOf(lastLabel));
    }

    private String buildCategorySummary(
            QueryResult result,
            int metricIndex) {

        List<List<Object>> rows = result.getRows();

        int categoryIndex =
                metricIndex == 0 ? 1 : 0;

        String metricLabel =
                describeMetric(result.getColumns(), metricIndex);

        Optional<List<Object>> maxRow =
                rows.stream()
                        .max(Comparator.comparingDouble(
                                row -> toDouble(row.get(metricIndex))));

        Optional<List<Object>> minRow =
                rows.stream()
                        .min(Comparator.comparingDouble(
                                row -> toDouble(row.get(metricIndex))));

        if (maxRow.isPresent() && minRow.isPresent()) {

            Object topCategory =
                    maxRow.get().get(categoryIndex);

            Object bottomCategory =
                    minRow.get().get(categoryIndex);

            double topValue =
                    toDouble(maxRow.get().get(metricIndex));

            double bottomValue =
                    toDouble(minRow.get().get(metricIndex));

            return String.format(
                    "%s is highest for %s at %.2f, while %s is lowest at %.2f.",
                    capitalize(metricLabel),
                    String.valueOf(topCategory),
                    topValue,
                    String.valueOf(bottomCategory),
                    bottomValue);
        }

        return buildDefaultSummary(result);
    }

    private String buildMetricSummary(
            QueryResult result,
            int metricIndex) {

        List<List<Object>> rows = result.getRows();

        double total =
                rows.stream()
                        .mapToDouble(
                                row -> toDouble(row.get(metricIndex)))
                        .sum();

        String metricLabel =
                describeMetric(result.getColumns(), metricIndex);

        return String.format(
                "The total %s across returned results is %.2f.",
                metricLabel,
                total);
    }

    private String buildDefaultSummary(QueryResult result) {

        if (result.getColumns().size() == 1) {

            return String.format(
                    "The query returned %d values for %s.",
                    result.getRowCount(),
                    result.getColumns().get(0));
        }

        return String.format(
                "The query returned %d rows across %d columns.",
                result.getRowCount(),
                result.getColumns().size());
    }

    private int findMetricColumn(QueryResult result) {

        List<String> columns = result.getColumns();

        for (int i = 0; i < columns.size(); i++) {

            if (isNumericColumn(result, i)) {
                return i;
            }
        }

        return -1;
    }

    private boolean looksLikeTimeSeries(QueryResult result) {

        if (result.getColumns().size() < 2) {
            return false;
        }

        if (!isNumericColumn(result, 1)) {
            return false;
        }

        return result.getRows().stream()
                .map(row -> row.get(0))
                .allMatch(this::isDateLike);
    }

    private boolean isNumericColumn(
            QueryResult result,
            int index) {

        return result.getRows().stream()
                .map(row -> row.get(index))
                .allMatch(this::isNumericValue);
    }

    private boolean isNumericValue(Object value) {

        if (value == null) {
            return false;
        }

        if (value instanceof Number) {
            return true;
        }

        try {
            Double.parseDouble(value.toString());
            return true;
        } catch (NumberFormatException ex) {
            return false;
        }
    }

    private double toDouble(Object value) {

        if (value == null) {
            return 0.0;
        }

        if (value instanceof Number number) {
            return number.doubleValue();
        }

        try {
            return Double.parseDouble(value.toString());
        } catch (NumberFormatException ex) {
            return 0.0;
        }
    }

    private boolean isDateLike(Object value) {

        if (value == null) {
            return false;
        }

        if (value instanceof Date
                || value instanceof Time
                || value instanceof Timestamp) {

            return true;
        }

        String text = value.toString();

        return DATE_PATTERN.matcher(text).matches()
                || DATETIME_PATTERN.matcher(text).matches();
    }

    private String describeMetric(
            List<String> columns,
            int metricIndex) {

        String label = columns.get(metricIndex);

        if (label == null || label.isBlank()) {
            return "metric";
        }

        return label.replaceAll("[_-]", " ")
                .toLowerCase(Locale.ROOT);
    }

    private String capitalize(String text) {

        if (text == null || text.isBlank()) {
            return "";
        }

        return Character.toUpperCase(text.charAt(0))
                + text.substring(1);
    }
}
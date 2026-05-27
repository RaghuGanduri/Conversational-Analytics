package com.hotbutton.analytics.service;

import com.hotbutton.analytics.dto.ChartMetadata;
import com.hotbutton.analytics.dto.QueryResult;

import org.springframework.stereotype.Service;

import java.sql.Date;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Collectors;


@Service
public class ChartMetadataService {

    private static final String BAR = "bar";
    private static final String LINE = "line";
    private static final String PIE = "pie";
    private static final String TABLE = "table";

    private static final int PIE_ROW_THRESHOLD = 5;

    private static final Pattern DATE_PATTERN =
            Pattern.compile("\\d{4}-\\d{2}-\\d{2}");

    private static final Pattern DATETIME_PATTERN =
            Pattern.compile("\\d{4}-\\d{2}-\\d{2}[ T]\\d{2}:\\d{2}(:\\d{2})?");

    public ChartMetadata buildChartMetadata(QueryResult result) {

        if (result == null
                || result.getColumns() == null
                || result.getColumns().isEmpty()) {

            return ChartMetadata.builder()
                    .chartType(TABLE)
                    .build();
        }

        if (result.getRows() == null
                || result.getRows().isEmpty()) {

            return ChartMetadata.builder()
                    .chartType(TABLE)
                    .labels(result.getColumns())
                    .build();
        }

        String chartType = determineChartType(result);

        if (TABLE.equals(chartType)) {

            return ChartMetadata.builder()
                    .chartType(TABLE)
                    .title("Tabular Results")
                    .build();
        }

        int metricIndex = findMetricColumn(result);

        if (metricIndex < 0) {

            return ChartMetadata.builder()
                    .chartType(TABLE)
                    .title("Unsupported Visualization")
                    .build();
        }

        List<String> labels = new ArrayList<>();

        List<Number> values = new ArrayList<>();

        for (List<Object> row : result.getRows()) {

            String label = row.subList(0, metricIndex)
            .stream()
            .map(String::valueOf)
            .collect(Collectors.joining(" - "));

            labels.add(label);

            Object metricValue = row.get(metricIndex);

            if (metricValue instanceof Number number) {
                values.add(number);
            } else {
                try {
                    values.add(Double.parseDouble(metricValue.toString()));
                } catch (Exception ex) {
                    values.add(0);
                }
            }
        }

        return ChartMetadata.builder()
                .chartType(chartType)
                .labels(labels)
                .values(values)
                .xAxis(result.getColumns().get(0))
                .yAxis(result.getColumns().get(metricIndex))
                .title(generateTitle(result))
                .build();
    }

    private String determineChartType(QueryResult result) {

        if (result.getColumns().size() == 1) {
            return TABLE;
        }

        if (looksLikeTimeSeries(result)) {
            return LINE;
        }

        if (result.getColumns().size() == 2
                && isMetricColumnNumeric(result, 1)) {

            //return result.getRows().size() <= PIE_ROW_THRESHOLD
            //        ? PIE
            //        : BAR;
            return BAR;
        }

        return TABLE;
    }

    private boolean looksLikeTimeSeries(QueryResult result) {

        if (result.getColumns().size() < 2
                || !isMetricColumnNumeric(result, 1)) {

            return false;
        }

        for (List<Object> row : result.getRows()) {

            Object label = row.get(0);

            if (!isDateLike(label)) {
                return false;
            }
        }

        return true;
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

        return DATE_PATTERN.matcher(value.toString()).matches()
                || DATETIME_PATTERN.matcher(value.toString()).matches();
    }

    private int findMetricColumn(QueryResult result) {

        if (result.getColumns().size() < 2) {
            return -1;
        }

        if (isMetricColumnNumeric(result, 1)) {
            return 1;
        }

        for (int i = 2; i < result.getColumns().size(); i++) {

            if (isMetricColumnNumeric(result, i)) {
                return i;
            }
        }

        return -1;
    }

    private boolean isMetricColumnNumeric(
            QueryResult result,
            int columnIndex) {

        return result.getRows().stream()
                .map(row -> row.get(columnIndex))
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

    private String generateTitle(QueryResult result) {

        if (result.getColumns().size() >= 2) {

            return result.getColumns().get(1)
                    + " by "
                    + result.getColumns().get(0);
        }

        return "Analytics Results";
    }
}
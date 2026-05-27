package com.hotbutton.analytics.service;

import java.util.List;
import java.util.Map;
import java.util.StringJoiner;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;

@Service
public class FilterResolver {

    public String buildWhereClause(Map<String, List<String>> filters) {
        if (filters == null || filters.isEmpty()) {
            return "";
        }

        StringJoiner conditions = new StringJoiner(" AND ");

        // collect date range parts separately to ensure deterministic ordering (from then to)
        java.util.Map<String, String> fromMap = new java.util.HashMap<>();
        java.util.Map<String, String> toMap = new java.util.HashMap<>();

        for (Map.Entry<String, List<String>> entry : filters.entrySet()) {
            String key = entry.getKey();
            List<String> values = entry.getValue();
            if (values == null || values.isEmpty()) {
                continue;
            }

            if (key.endsWith("_from")) {
                String column = key.substring(0, key.length() - 5);
                fromMap.put(column, quoteValue(values.get(0)));
                continue;
            }
            if (key.endsWith("_to")) {
                String column = key.substring(0, key.length() - 3);
                toMap.put(column, quoteValue(values.get(0)));
                continue;
            }

            if (values.size() == 1) {
                String v = values.get(0);
                if (isBoolean(v) || isNumeric(v)) {
                    conditions.add(key + " = " + v.trim());
                } else {
                    conditions.add(key + " = " + quoteValue(v));
                }
            } else {
                String joined = values.stream().map(this::quoteValue).collect(Collectors.joining(", ", "(", ")"));
                conditions.add(key + " IN " + joined);
            }
        }

        // append date ranges in deterministic order: sorted column names
        java.util.List<String> dateCols = new java.util.ArrayList<>();
        dateCols.addAll(fromMap.keySet());
        for (String k : toMap.keySet()) {
            if (!dateCols.contains(k)) dateCols.add(k);
        }
        java.util.Collections.sort(dateCols);
        for (String col : dateCols) {
            if (fromMap.containsKey(col)) {
                conditions.add(col + " >= " + fromMap.get(col));
            }
            if (toMap.containsKey(col)) {
                conditions.add(col + " <= " + toMap.get(col));
            }
        }

        return conditions.length() == 0 ? "" : " WHERE " + conditions.toString();
    }

    private String quoteValue(String value) {
        if (value == null) {
            return "NULL";
        }
        String trimmed = value.trim();
        if (isBoolean(trimmed) || isNumeric(trimmed)) {
            return trimmed;
        }
        return "'" + trimmed.replace("'", "''") + "'";
    }

    private boolean isNumeric(String value) {
        return value != null && value.matches("^-?\\d+(\\\\.\\d+)?$");
    }

    private boolean isBoolean(String value) {
        if (value == null) return false;
        String v = value.trim().toLowerCase();
        return "true".equals(v) || "false".equals(v);
    }
}

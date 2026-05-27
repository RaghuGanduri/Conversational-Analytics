package com.hotbutton.analytics.service;

import com.hotbutton.analytics.config.AnalyticsProperties;
import com.hotbutton.analytics.dto.QueryResult;
import com.hotbutton.analytics.dto.SqlValidationResult;
import com.hotbutton.analytics.exception.AnalyticsException;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCallback;
import org.springframework.stereotype.Service;

@Service
public class QueryExecutionService {

    private final JdbcTemplate jdbcTemplate;
    private final AnalyticsProperties properties;

    public QueryExecutionService(JdbcTemplate jdbcTemplate, AnalyticsProperties properties) {
        this.jdbcTemplate = jdbcTemplate;
        this.properties = properties;
    }

    public QueryResult execute(String validatedSql) {
        String sql = validatedSql.trim();
        long startNanos = System.nanoTime();
        try {
            return jdbcTemplate.execute(sql, (PreparedStatementCallback<QueryResult>) ps -> {
                ps.setQueryTimeout(properties.getQueryTimeoutSeconds());
                try (ResultSet rs = ps.executeQuery()) {
                    ResultSetMetaData metaData = rs.getMetaData();
                    int columnCount = metaData.getColumnCount();
                    List<String> columns = new ArrayList<>(columnCount);
                    for (int i = 1; i <= columnCount; i++) {
                        columns.add(metaData.getColumnLabel(i));
                    }

                    List<List<Object>> rows = new ArrayList<>();
                    while (rs.next()) {
                        List<Object> row = new ArrayList<>(columnCount);
                        for (int i = 1; i <= columnCount; i++) {
                            row.add(rs.getObject(i));
                        }
                        rows.add(row);
                    }
                    long elapsedMillis = TimeUnit.NANOSECONDS.toMillis(System.nanoTime() - startNanos);
                    return new QueryResult(columns, rows, elapsedMillis, rows.size());
                }
            });
        } catch (DataAccessException ex) {
            throw new AnalyticsException("QUERY_EXECUTION", "Failed to execute SQL: " + ex.getMessage());
        }
    }
}

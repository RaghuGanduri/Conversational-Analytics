package com.hotbutton.analytics.config;

import java.util.ArrayList;
import java.util.List;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "analytics")
public class AnalyticsProperties {

    private int queryTimeoutSeconds = 10;
    private int maxRows = 5000;
    private int defaultLimit = 5000;
    private SqlGenerationMode sqlGenerationMode = SqlGenerationMode.AUTO;
    private List<String> allowedTables = new ArrayList<>(List.of("part_claim", "claim_escalation"));

    public int getQueryTimeoutSeconds() {
        return queryTimeoutSeconds;
    }

    public void setQueryTimeoutSeconds(int queryTimeoutSeconds) {
        this.queryTimeoutSeconds = queryTimeoutSeconds;
    }

    public int getMaxRows() {
        return maxRows;
    }

    public void setMaxRows(int maxRows) {
        this.maxRows = maxRows;
    }

    public int getDefaultLimit() {
        return defaultLimit;
    }

    public void setDefaultLimit(int defaultLimit) {
        this.defaultLimit = defaultLimit;
    }

    public SqlGenerationMode getSqlGenerationMode() {
        return sqlGenerationMode;
    }

    public void setSqlGenerationMode(SqlGenerationMode sqlGenerationMode) {
        this.sqlGenerationMode = sqlGenerationMode;
    }

    public List<String> getAllowedTables() {
        return allowedTables;
    }

    public void setAllowedTables(List<String> allowedTables) {
        this.allowedTables = allowedTables;
    }

    public enum SqlGenerationMode {
        AUTO,
        OLLAMA,
        TEMPLATE
    }
}

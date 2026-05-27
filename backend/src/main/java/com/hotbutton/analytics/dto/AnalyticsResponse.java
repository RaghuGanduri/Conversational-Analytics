package com.hotbutton.analytics.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AnalyticsResponse {

    @Builder.Default
    private Boolean success = true;

    @NotBlank
    private String chartType;

    @Builder.Default
    @NotNull
    private List<String> labels = new ArrayList<>();

    @Builder.Default
    @NotNull
    private List<Number> values = new ArrayList<>();

    private String summary;

    private String generatedSql;

    private SemanticQuery semanticQuery;

    @Builder.Default
    @NotNull
    private List<String> columns = new ArrayList<>();

    @Builder.Default
    @NotNull
    private List<List<Object>> rows = new ArrayList<>();

    private Long executionTimeMs;

    private Integer rowCount;

    private String errorMessage;

    private String requestId;
}
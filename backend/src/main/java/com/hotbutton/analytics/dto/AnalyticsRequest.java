package com.hotbutton.analytics.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AnalyticsRequest {

    @NotBlank
    private String prompt;

    private String preferredChartType;

    @Min(1)
    @Builder.Default
    private Integer maxRows = 5000;

    @Builder.Default
    private Boolean includeGeneratedSql = false;

    @Builder.Default
    private Boolean includeSummary = true;

    @Builder.Default
    private Boolean debugMode = false;
}

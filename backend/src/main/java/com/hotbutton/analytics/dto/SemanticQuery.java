package com.hotbutton.analytics.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SemanticQuery {

    //@NotBlank
    //private String table;

    @Builder.Default
    @NotNull
    private List<String> dimensions = new ArrayList<>();

    @NotBlank
    private String metric;

    private String metricColumn;

    @Builder.Default
    @NotNull
    private List<String> selectColumns = new ArrayList<>();

    @Builder.Default
    @NotNull
    private Map<String, List<String>> filters = new HashMap<>();

    @Min(1)
    @Max(5000)
    @Builder.Default
    private Integer limit = 5000;

    private String chartType;

    private String domain;
}
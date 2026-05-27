package com.hotbutton.analytics.dto;

import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class QueryResult {

    @Builder.Default
    @NotNull
    private List<String> columns = new ArrayList<>();

    @Builder.Default
    @NotNull
    private List<List<Object>> rows = new ArrayList<>();

    private Long executionTimeMs;

    private Integer rowCount;
}
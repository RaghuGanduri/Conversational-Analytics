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
public class ChartMetadata {

    @NotBlank
    private String chartType;

    @Builder.Default
    @NotNull
    private List<String> labels = new ArrayList<>();

    @Builder.Default
    @NotNull
    private List<Number> values = new ArrayList<>();

    private String xAxis;

    private String yAxis;

    private String title;
}
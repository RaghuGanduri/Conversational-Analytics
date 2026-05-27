package com.hotbutton.analytics.metadata;

import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SemanticColumn {

    private String name;

    private String type;

    private String description;

    private String semanticType;

    @Builder.Default
    private List<String> synonyms = new ArrayList<>();

    @Builder.Default
    private List<String> allowedValues = new ArrayList<>();

    @Builder.Default
    private List<String> aggregations = new ArrayList<>();

    @Builder.Default
    private Boolean filterable = false;

    @Builder.Default
    private Boolean groupable = false;

    @Builder.Default
    private Boolean aggregatable = false;
}
package com.hotbutton.analytics.metadata;

import lombok.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BusinessTerm {

    private String term;

    private String meaning;

    private String category;

    @Builder.Default
    private List<String> allowedValues = new ArrayList<>();

    @Builder.Default
    private Map<String, String> mapsTo = Map.of();
}
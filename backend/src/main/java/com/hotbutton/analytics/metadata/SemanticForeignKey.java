package com.hotbutton.analytics.metadata;

import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SemanticForeignKey {

    private String sourceColumn;

    private String targetTable;

    private String targetColumn;

    private String relationshipType;
}
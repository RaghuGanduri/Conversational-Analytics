package com.hotbutton.analytics.metadata;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SemanticTable {

    @NotBlank
    private String tableName;

    private String description;

    private String businessPurpose;

    @Builder.Default
    @NotNull
    private List<SemanticColumn> columns = new ArrayList<>();

    @Builder.Default
    @NotNull
    private List<String> dimensions = new ArrayList<>();

    @Builder.Default
    @NotNull
    private List<String> metrics = new ArrayList<>();

    @Builder.Default
    @NotNull
    private List<SemanticForeignKey> foreignKeys = new ArrayList<>();
}
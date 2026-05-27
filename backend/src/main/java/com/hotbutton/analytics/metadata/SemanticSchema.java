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
public class SemanticSchema {

    @NotBlank
    private String database;

    @Builder.Default
    @NotNull
    private List<SemanticTable> tables = new ArrayList<>();
}
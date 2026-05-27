package com.hotbutton.analytics.dto;

import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SqlValidationResult {

    @Builder.Default
    private Boolean valid = true;

    private String sql;

    private String sanitizedSql;

    @Builder.Default
    private List<String> validationErrors = new ArrayList<>();

    @Builder.Default
    private List<String> blockedKeywords = new ArrayList<>();
}
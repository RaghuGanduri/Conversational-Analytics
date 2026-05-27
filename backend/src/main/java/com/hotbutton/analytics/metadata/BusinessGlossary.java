package com.hotbutton.analytics.metadata;

import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BusinessGlossary {

    @Builder.Default
    @NotNull
    private List<BusinessTerm> businessTerms = new ArrayList<>();
}
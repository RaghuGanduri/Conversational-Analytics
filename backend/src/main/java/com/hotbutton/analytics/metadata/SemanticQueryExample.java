package com.hotbutton.analytics.metadata;

import com.hotbutton.analytics.dto.SemanticQuery;
import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SemanticQueryExample {

    private String question;

    private SemanticQuery semanticQuery;
}
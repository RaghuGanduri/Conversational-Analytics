package com.hotbutton.analytics.service;

import static org.assertj.core.api.Assertions.assertThat;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.Map;
import org.junit.jupiter.api.Test;

class PromptTemplateServiceTest {

    private final PromptTemplateService service = new PromptTemplateService(new ObjectMapper());

    @Test
    void loadsAndBuildsSemanticQueryTemplate() {
        String prompt = service.buildPrompt("symantic-query-generator", Map.of(
                "schema", "SCHEMA",
                "glossary", "GLOSS",
                "examples", "EX",
                "question", "Show claims for Honda"
        ));
        assertThat(prompt).contains("SEMANTIC QUERY FORMAT");
        assertThat(prompt).contains("SCHEMA");
        assertThat(prompt).contains("GLOSS");
        assertThat(prompt).contains("EX");
        assertThat(prompt).contains("Show claims for Honda");
    }
}

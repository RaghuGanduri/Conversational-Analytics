package com.hotbutton.analytics.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import com.hotbutton.analytics.metadata.BusinessGlossary;
import com.hotbutton.analytics.metadata.SemanticQueryExample;
import com.hotbutton.analytics.metadata.SemanticSchema;

import lombok.RequiredArgsConstructor;

import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PromptTemplateService {

    private final SemanticContextLoader semanticContextLoader;

    private final ObjectMapper objectMapper;

    public String buildSemanticPrompt(String userPrompt) {

        try {

            String template =
                    loadTemplate("prompts/semantic-query-generator.st");

            String semanticSchemaJson =
                    buildCompactSchema();

            String glossaryJson =
                    toPrettyJson(
                            semanticContextLoader.getGlossary());

            String examplesJson =
                    toPrettyJson(
                            semanticContextLoader.getExamples());

            return template
                    .replace("<schema>", semanticSchemaJson)
                    .replace("<glossary>", glossaryJson)
                    .replace("<examples>", examplesJson)
                    .replace("<prompt>", userPrompt);

        } catch (Exception ex) {

            throw new RuntimeException(
                    "Failed to build semantic prompt",
                    ex);
        }
    }

    private String buildCompactSchema() {

        return """
            Domains:

            claims:
            - table: part_claim
            - dimensions: severity, oem_name, part_number
            - metrics: claim_cost

            escalations:
            - table: claim_escalation
            - dimensions: sla_breached_flag
        """;
}

    private String loadTemplate(String path) {

        try {

            ClassPathResource resource =
                    new ClassPathResource(path);

            try (InputStream inputStream =
                         resource.getInputStream()) {

                return new String(
                        inputStream.readAllBytes(),
                        StandardCharsets.UTF_8);
            }

        } catch (IOException ex) {

            throw new RuntimeException(
                    "Failed to load prompt template: " + path,
                    ex);
        }
    }

    private String toPrettyJson(Object object) {

        try {

            return objectMapper.writerWithDefaultPrettyPrinter()
                    .writeValueAsString(object);

        } catch (JsonProcessingException ex) {

            throw new RuntimeException(
                    "Failed to serialize metadata",
                    ex);
        }
    }
}
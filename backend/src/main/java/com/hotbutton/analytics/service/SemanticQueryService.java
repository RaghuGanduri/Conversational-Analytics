package com.hotbutton.analytics.service;

import com.fasterxml.jackson.databind.ObjectMapper;

import com.hotbutton.analytics.dto.SemanticQuery;

import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SemanticQueryService {

    private static final Logger log =
            LoggerFactory.getLogger(SemanticQueryService.class);

    private final ChatClient chatClient;

    private final PromptTemplateService promptTemplateService;

    private final ObjectMapper objectMapper;

    public SemanticQuery buildSemanticQuery(String prompt) {

        try {

            String finalPrompt =
                    promptTemplateService.buildSemanticPrompt(prompt);

            log.info("Sending prompt to LLM");

            String response =
                    chatClient.prompt()
                            .user(finalPrompt)
                            .call()
                            .content();

            log.info("LLM Semantic Response: {}", response);

            String json =
                    sanitizeJson(
                            extractJson(response));

            log.info("Extracted JSON: {}", json);

            SemanticQuery semanticQuery =
                    objectMapper.readValue(
                            json,
                            SemanticQuery.class);

            SemanticQuery normalized =
                    normalizeSemanticQuery(
                            semanticQuery);

            log.info(
                    "Normalized SemanticQuery: {}",
                    normalized);

            return normalized;

        } catch (Exception ex) {

            throw new RuntimeException(
                    "Failed to build semantic query",
                    ex);
        }
    }

    private SemanticQuery normalizeSemanticQuery(
            SemanticQuery query) {

        if (query == null) {
            return null;
        }

        /*
         * Default domain
         */
        if (query.getDomain() == null
                || query.getDomain().isBlank()) {

            query.setDomain("claims");
        }

        /*
         * Default collections
         */
        if (query.getDimensions() == null) {
            query.setDimensions(List.of());
        }

        if (query.getSelectColumns() == null) {
            query.setSelectColumns(List.of());
        }

        if (query.getFilters() == null) {
            query.setFilters(Map.of());
        }

        /*
         * Default limit
         */
        if (query.getLimit() == null
                || query.getLimit() <= 0) {

            query.setLimit(5000);
        }

        /*
 * Auto-infer aggregation
 */
if ((query.getMetric() == null
        || query.getMetric().isBlank())
        &&
        query.getDimensions() != null
        &&
        !query.getDimensions().isEmpty()) {

    query.setMetric("count");

    query.setMetricColumn("*");
}

        /*
         * Determine metric existence
         */
        boolean hasMetric =
                query.getMetric() != null
                        && !query.getMetric().isBlank();

        boolean hasDimensions =
                query.getDimensions() != null
                        && !query.getDimensions().isEmpty();

        boolean hasSelectColumns =
                query.getSelectColumns() != null
                        && !query.getSelectColumns().isEmpty();

        /*
         * Aggregation query validation
         */
        if (hasMetric && !hasDimensions) {

            throw new RuntimeException(
                    "Aggregated query requires dimensions");
        }

        /*
         * Aggregation queries:
         * select columns should become dimensions
         */
        if (hasMetric && hasDimensions) {

            query.setSelectColumns(
                    query.getDimensions());
        }

        /*
         * Default row-level columns
         */
        if (!hasMetric && !hasSelectColumns) {

            query.setSelectColumns(
                    List.of(
                            "claim_id",
                            "part_number",
                            "severity",
                            "oem_name",
                            "claim_cost"));
        }

        /*
         * Smart chart type defaulting
         */
        if (query.getChartType() == null
                || query.getChartType().isBlank()) {

            if (hasMetric && hasDimensions) {

                query.setChartType("bar");

            } else {

                query.setChartType("table");
            }
        }

        /*
         * Safety normalization
         */
        if ("table".equalsIgnoreCase(
                query.getChartType())) {

            query.setDimensions(List.of());

            query.setMetric("");

            query.setMetricColumn("");
        }

        return query;
    }

    private String extractJson(String response) {

    if (response == null || response.isBlank()) {

        throw new RuntimeException(
                "LLM response is empty");
    }

    String cleaned =
            response
                    .replace("```json", "")
                    .replace("```", "")
                    .trim();

    /*
     * Find JSON starting point
     */
    int start =
            cleaned.indexOf("{");

    if (start < 0) {

        throw new RuntimeException(
                "No JSON found in response");
    }

    cleaned = cleaned.substring(start);

    /*
     * Auto-fix missing braces
     */
    long openBraces =
            cleaned.chars()
                    .filter(ch -> ch == '{')
                    .count();

    long closeBraces =
            cleaned.chars()
                    .filter(ch -> ch == '}')
                    .count();

    while (closeBraces < openBraces) {

        cleaned += "}";

        closeBraces++;
    }

    /*
     * Auto-fix missing arrays
     */
    long openBrackets =
            cleaned.chars()
                    .filter(ch -> ch == '[')
                    .count();

    long closeBrackets =
            cleaned.chars()
                    .filter(ch -> ch == ']')
                    .count();

    while (closeBrackets < openBrackets) {

        cleaned += "]";

        closeBrackets++;
    }

    return cleaned;
    }

    private String sanitizeJson(String json) {

        if (json == null) {
            return null;
        }

        return json
                .replace("ô", "\"")
                .replace("ö", "\"")
                .replace("“", "\"")
                .replace("”", "\"")
                .replace("‘", "'")
                .replace("’", "'");
    }
}
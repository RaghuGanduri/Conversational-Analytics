package com.hotbutton.analytics.service;

import com.hotbutton.analytics.dto.AnalyticsResponse;
import com.hotbutton.analytics.dto.ChartMetadata;
import com.hotbutton.analytics.dto.QueryResult;
import com.hotbutton.analytics.dto.SemanticQuery;
import com.hotbutton.analytics.dto.SqlValidationResult;

import lombok.RequiredArgsConstructor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AnalyticsOrchestratorService {

    private static final Logger log =
            LoggerFactory.getLogger(AnalyticsOrchestratorService.class);

    private final SemanticQueryService semanticQueryService;

    private final SqlBuilderService sqlBuilderService;

    private final SqlValidationService sqlValidationService;

    private final QueryExecutionService queryExecutionService;

    private final ChartMetadataService chartMetadataService;

    private final InsightGenerationService insightGenerationService;

    public AnalyticsResponse process(String prompt) {

        SemanticQuery semanticQuery =
                semanticQueryService.buildSemanticQuery(prompt);

        log.info("Generated SemanticQuery: {}", semanticQuery);

        String generatedSql =
                sqlBuilderService.buildSql(semanticQuery);

        log.info("Generated SQL: {}", generatedSql);

        SqlValidationResult validationResult =
                sqlValidationService.validate(generatedSql);

        if (!Boolean.TRUE.equals(validationResult.getValid())) {

            return AnalyticsResponse.builder()
                    .success(false)
                    .errorMessage(
                            String.join(
                                    ", ",
                                    validationResult.getValidationErrors()))
                    .generatedSql(generatedSql)
                    .semanticQuery(semanticQuery)
                    .build();
        }

        QueryResult queryResult =
                queryExecutionService.execute(
                        validationResult.getSanitizedSql());

        ChartMetadata metadata =
                chartMetadataService.buildChartMetadata(queryResult);

        String summary =
                insightGenerationService.generate(queryResult);

        return AnalyticsResponse.builder()
                .success(true)
                .chartType(metadata.getChartType())
                .labels(metadata.getLabels())
                .values(metadata.getValues())
                .summary(summary)
                .generatedSql(generatedSql)
                .semanticQuery(semanticQuery)
                .columns(queryResult.getColumns())
                .rows(queryResult.getRows())
                .rowCount(queryResult.getRowCount())
                .executionTimeMs(queryResult.getExecutionTimeMs())
                .build();
    }
}
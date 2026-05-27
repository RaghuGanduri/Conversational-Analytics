# Conversational Analytics Platform � Tasks

# Phase 1 � Foundation Setup

## Task 1 � Setup Spring Boot Backend
- [ ] Create Spring Boot project
- [ ] Add Spring Web dependency
- [ ] Add Spring AI dependency
- [ ] Add PostgreSQL dependency
- [ ] Add Lombok dependency
- [ ] Configure application.yml

---

## Task 2 � Setup Angular Frontend
- [ ] Create Angular application
- [ ] Install Angular Material
- [ ] Install ngx-echarts
- [ ] Configure routing
- [ ] Configure API integration

---

## Task 3 � Setup PostgreSQL Database
- [ ] Create database
- [ ] Create part_claim table
- [ ] Create claim_escalation table
- [ ] Create indexes
- [ ] Insert sample data

---

## Task 4 � Setup AI Provider
- [ ] Configure Azure OpenAI
- [ ] Configure Ollama fallback
- [ ] Verify AI connectivity
- [ ] Test AI prompts

---

# Phase 2 � Semantic Metadata

## Task 5 � Create semantic-schema.json
- [ ] Define table metadata
- [ ] Define column metadata
- [ ] Define relationships
- [ ] Define synonyms
- [ ] Define allowed values

---

## Task 6 � Create business-glossary.json
- [ ] Add claims terminology
- [ ] Add escalation terminology
- [ ] Add OEM terminology
- [ ] Add hot button indicator terminology

---

## Task 7 � Create semantic-query-examples.json
- [ ] Add claims examples
- [ ] Add severity examples
- [ ] Add OEM filter examples
- [ ] Add escalation examples
- [ ] Add aggregation examples
- [ ] Add multi-filter examples

---

# Phase 3 � Prompt Engineering

## Task 8 � Create semantic-query-generator.st
- [ ] Define SemanticQuery rules
- [ ] Define JSON output rules
- [ ] Add semantic examples
- [ ] Add schema placeholders
- [ ] Add glossary placeholders

---

## Task 9 � Create chart-generator.st
- [ ] Define chart recommendation rules
- [ ] Add aggregation detection rules
- [ ] Add chart mapping rules

---

## Task 10 � Create summary-generator.st
- [ ] Generate executive summaries
- [ ] Generate trend insights
- [ ] Generate anomaly summaries

---

# Phase 4 � Core Backend Development

## Task 11 � Create SemanticQuery DTO
- [ ] Add domain field
- [ ] Add metric field
- [ ] Add dimensions field
- [ ] Add filters field
- [ ] Add chartType field
- [ ] Add limit field

---

## Task 12 — Create AnalyticsRequest DTO
- [ ] Create AnalyticsRequest DTO
- [ ] Add prompt field
- [ ] Add preferredChartType field
- [ ] Add maxRows field
- [ ] Add includeGeneratedSql field
- [ ] Add includeSummary field
- [ ] Add debugMode field
- [ ] Add validation annotations
- [ ] Use Lombok annotations
- [ ] Use Builder pattern

---

## Task 13 — Create AnalyticsResponse DTO
- [ ] Create AnalyticsResponse DTO
- [ ] Add success field
- [ ] Add chartType field
- [ ] Add labels field
- [ ] Add values field
- [ ] Add summary field
- [ ] Add generatedSql field
- [ ] Add semanticQuery field
- [ ] Add columns field
- [ ] Add rows field
- [ ] Add executionTimeMs field
- [ ] Add rowCount field
- [ ] Add errorMessage field
- [ ] Add requestId field
- [ ] Add validation annotations
- [ ] Use Lombok annotations
- [ ] Use Builder pattern
- [ ] Use Builder.Default for collections

---

## Task 14 � Create QueryResult DTO
- [ ] Add columns
- [ ] Add rows
- [ ] Add executionTime
- [ ] Add rowCount

---

## Task 15 � Create ChartMetadata DTO
- [ ] Add chartType
- [ ] Add labels
- [ ] Add values

---

## Task 15C — Create SemanticForeignKey Metadata Model
- [ ] Create SemanticForeignKey model
- [ ] Add sourceColumn field
- [ ] Add targetTable field
- [ ] Add targetColumn field
- [ ] Add relationshipType field
- [ ] Use Lombok annotations
- [ ] Use Builder pattern

---

## Task 15D — Create SqlValidationResult DTO
- [ ] Create SqlValidationResult DTO
- [ ] Add valid field
- [ ] Add sql field
- [ ] Add validationErrors field
- [ ] Add blockedKeywords field
- [ ] Add sanitizedSql field
- [ ] Use Lombok annotations
- [ ] Use Builder pattern
- [ ] Use Builder.Default for collections

---

## Task 16 � Create SemanticContextLoader
- [ ] Load semantic-schema.json
- [ ] Load business-glossary.json
- [ ] Load semantic-query-examples.json

---

## Task 17 � Create PromptTemplateService
- [ ] Load prompt templates
- [ ] Replace placeholders
- [ ] Build final prompts

---

## Task 18 � Create SemanticQueryService
- [ ] Invoke Spring AI
- [ ] Invoke Azure OpenAI/Ollama
- [ ] Generate SemanticQuery JSON
- [ ] Parse AI response
- [ ] Handle AI errors

---

## Task 19 � Create FilterResolver
- [ ] Resolve OEM filters
- [ ] Resolve severity filters
- [ ] Resolve escalation filters
- [ ] Resolve date filters

---

## Task 20 � Create AggregationResolver
- [ ] Resolve COUNT metrics
- [ ] Resolve SUM metrics
- [ ] Resolve AVG metrics
- [ ] Resolve aliases

---

## Task 21 � Create SqlBuilderService
- [ ] Build SELECT clause
- [ ] Build aggregate clause
- [ ] Build WHERE clause
- [ ] Build GROUP BY clause
- [ ] Build ORDER BY clause
- [ ] Build LIMIT clause

---

## Task 22 � Create SqlOptimizationService
- [ ] Inject missing LIMIT
- [ ] Normalize aliases
- [ ] Optimize GROUP BY queries
- [ ] Optimize aggregation queries

---

## Task 23 � Create SqlValidationService
- [ ] Block UPDATE
- [ ] Block DELETE
- [ ] Block INSERT
- [ ] Block DROP
- [ ] Block ALTER
- [ ] Block TRUNCATE
- [ ] Validate allowed tables
- [ ] Validate allowed columns

---

## Task 24 � Create QueryExecutionService
- [ ] Execute validated SQL
- [ ] Apply timeout handling
- [ ] Build QueryResult

---

## Task 25 � Create ChartMetadataService
- [ ] Detect chartable data
- [ ] Build bar chart metadata
- [ ] Build line chart metadata
- [ ] Build pie chart metadata
- [ ] Build table metadata

---

## Task 26 � Create InsightGenerationService
- [ ] Generate executive summaries
- [ ] Generate trend insights
- [ ] Generate anomaly insights

---

## Task 27 � Create AnalyticsOrchestratorService
- [ ] Orchestrate analytics workflow
- [ ] Invoke SemanticQueryService
- [ ] Invoke SqlBuilderService
- [ ] Invoke validators
- [ ] Invoke execution services
- [ ] Build final response

---

## Task 28 � Create AnalyticsController
- [ ] Create POST /api/analytics/query
- [ ] Validate request
- [ ] Return analytics response

---

# Phase 5 � Frontend Development

## Task 29 � Create PromptInputComponent
- [ ] Add natural language textbox
- [ ] Add submit button
- [ ] Add loading indicator

---

## Task 30 � Create ChartViewComponent
- [ ] Render bar chart
- [ ] Render line chart
- [ ] Render pie chart
- [ ] Render table view

---

## Task 31 � Create InsightPanelComponent
- [ ] Display generated SQL
- [ ] Display SemanticQuery
- [ ] Display AI summary

---

## Task 32 � Create AnalyticsDashboardComponent
- [ ] Integrate prompt input
- [ ] Integrate chart view
- [ ] Integrate insight panel

---

# Phase 6 � Security & Governance

## Task 33 � Configure Read-Only Database User
- [ ] Create analytics_user
- [ ] Grant SELECT privileges only

---

## Task 34 � Add Query Governance
- [ ] Enforce row limits
- [ ] Enforce query timeout
- [ ] Restrict unauthorized tables
- [ ] Restrict unauthorized columns

---

## Task 35 � Add AI Guardrails
- [ ] Restrict unsafe prompts
- [ ] Restrict unsupported analytics
- [ ] Validate SemanticQuery JSON

---

# Phase 7 � Logging & Monitoring

## Task 36 � Add Application Logging
- [ ] Log prompts
- [ ] Log SemanticQuery
- [ ] Log generated SQL
- [ ] Log execution time

---

## Task 37 � Add Monitoring
- [ ] Track AI latency
- [ ] Track SQL latency
- [ ] Track failures
- [ ] Track chart generation failures

---

# Phase 8 � Advanced AI Enhancements

## Task 38 � Add AI Chart Recommendation
- [ ] AI-driven visualization recommendation
- [ ] Advanced chart selection

---

## Task 39 � Add RAG Support
- [ ] Add embeddings
- [ ] Add vector search
- [ ] Add semantic memory

---

## Task 40 � Add Predictive Analytics
- [ ] Forecasting
- [ ] Trend prediction
- [ ] Anomaly detection

---

## Task 41 � Add Multi-Agent Analytics
- [ ] Planner agent
- [ ] SQL optimization agent
- [ ] Visualization agent
- [ ] Insight generation agent

---

# Recommended Implementation Order

1. SemanticQuery DTO
2. SemanticContextLoader
3. PromptTemplateService
4. SemanticQueryService
5. FilterResolver
6. AggregationResolver
7. SqlBuilderService
8. SqlValidationService
9. QueryExecutionService
10. ChartMetadataService
11. AnalyticsOrchestratorService
12. AnalyticsController

---

# Recommended Copilot Prompts

## Example Prompts

- Implement Task 11 based on spec.md and plan.md
- Generate SemanticQuery DTO
- Implement deterministic SqlBuilderService
- Implement SemanticQueryService using Spring AI
- Implement FilterResolver
- Implement ChartMetadataService

---

# Recommended Development Strategy

DO NOT generate the entire project at once.

Implement incrementally:
- one DTO at a time
- one service at a time
- one layer at a time

Validate each phase before moving to the next phase.

---

# Most Important Architectural Principle

AI SHALL:
- understand semantics
- extract intent
- identify filters
- identify dimensions

Backend SHALL:
- generate deterministic SQL
- validate SQL
- enforce governance
- execute analytics safely
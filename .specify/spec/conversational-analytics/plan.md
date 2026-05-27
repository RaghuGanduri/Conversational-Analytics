# Conversational Analytics Platform — Implementation Plan

# Objective

Implement an enterprise semantic conversational analytics platform
using:
- Spring Boot
- Spring AI
- PostgreSQL
- Angular
- Azure OpenAI/Ollama

The platform shall support:
- conversational analytics
- semantic understanding
- deterministic SQL generation
- governed analytics execution
- dynamic visualization

---

# High-Level Architecture

Frontend (Angular)
    ↓
AnalyticsController
    ↓
AnalyticsOrchestratorService
    ↓
SemanticQueryService
    ↓
SemanticQuery Object
    ↓
SqlBuilderService
    ↓
SqlOptimizationService
    ↓
SqlValidationService
    ↓
QueryExecutionService
    ↓
ChartMetadataService
    ↓
InsightGenerationService
    ↓
Frontend Visualization

---

# Architecture Overview

## Frontend

Framework:
- Angular

Visualization:
- ngx-echarts

Responsibilities:
- accept natural language prompts
- render charts dynamically
- display insights
- display generated SQL
- display semantic query

---

## Backend

Framework:
- Spring Boot
- Spring AI

Responsibilities:
- semantic interpretation
- SQL generation
- validation
- execution
- chart recommendation
- insight generation

---

## AI Layer

Supported Providers:
- Azure OpenAI
- Ollama

Responsibilities:
- understand business language
- identify filters
- identify dimensions
- identify metrics
- generate SemanticQuery JSON

The AI SHALL NOT:
- execute SQL
- directly access database
- generate unsafe SQL

---

## Database Layer

Database:
- PostgreSQL

Responsibilities:
- claims data
- escalation data
- analytics execution

Access Strategy:
- read-only analytics user

---

# Architectural Principles

## Principle 1 — AI Understands Semantics

AI SHALL:
- understand natural language
- extract analytics intent
- identify filters
- identify aggregations
- identify dimensions
- recommend visualization

AI SHALL NOT:
- directly execute SQL
- mutate database
- generate unsafe SQL

---

## Principle 2 — Backend Generates SQL

The backend SHALL:
- generate deterministic SQL
- apply governance
- validate queries
- optimize queries
- enforce security

---

## Principle 3 — Security First

The platform SHALL:
- allow SELECT only
- restrict tables
- restrict columns
- enforce LIMIT
- apply query timeout
- use read-only DB access

---

# System Workflow

User Prompt
↓
Semantic Understanding
↓
SemanticQuery Generation
↓
Deterministic SQL Builder
↓
SQL Optimization
↓
SQL Validation
↓
PostgreSQL Execution
↓
Chart Recommendation
↓
Insight Generation
↓
Frontend Rendering

---

# Semantic Query Flow

## User Prompt

Example:

```text
Show claims by severity for Honda and Ford
```

---

## SemanticQuery Generation

AI returns:

```json
{
  "domain": "claims",
  "metric": "count",
  "dimensions": ["severity"],
  "filters": {
    "oem_name": ["Honda", "Ford"]
  },
  "chartType": "bar",
  "limit": 5000
}
```

---

## Deterministic SQL Builder

Backend generates:

```sql
SELECT severity,
       COUNT(*) AS claim_count
FROM part_claim
WHERE oem_name IN ('Honda', 'Ford')
GROUP BY severity
ORDER BY claim_count DESC
LIMIT 5000
```

---

# Core Backend Components

## AnalyticsController

Responsibilities:
- expose REST APIs
- validate requests
- return analytics response

---

## AnalyticsOrchestratorService

Responsibilities:
- orchestrate analytics workflow
- invoke semantic interpretation
- invoke SQL builder
- invoke validators
- invoke execution services
- build final response

---

## SemanticQueryService

Responsibilities:
- invoke Spring AI
- invoke Azure OpenAI/Ollama
- generate SemanticQuery JSON
- parse AI response
- handle AI errors

---

## SemanticContextLoader

Responsibilities:
- load semantic-schema.json
- load business-glossary.json
- load semantic-query-examples.json

---

## PromptTemplateService

Responsibilities:
- load prompt templates
- inject placeholders
- build final prompts

---

## SqlBuilderService

Responsibilities:
- generate deterministic SQL
- build SELECT clause
- build WHERE clause
- build GROUP BY clause
- build ORDER BY clause
- build LIMIT clause

---

## FilterResolver

Responsibilities:
- resolve OEM filters
- resolve severity filters
- resolve date filters
- resolve escalation filters

---

## AggregationResolver

Responsibilities:
- resolve COUNT metrics
- resolve SUM metrics
- resolve AVG metrics
- resolve aliases

---

## SqlOptimizationService

Responsibilities:
- normalize aliases
- inject missing LIMIT
- optimize GROUP BY queries
- optimize aggregations

---

## SqlValidationService

Responsibilities:
- validate SQL safety
- block DML/DDL
- validate allowed tables
- validate allowed columns

---

## QueryExecutionService

Responsibilities:
- execute validated SQL
- apply timeout
- build QueryResult

---

## ChartMetadataService

Responsibilities:
- determine chart type
- build labels
- build values
- support:
  - bar chart
  - line chart
  - pie chart
  - table view

---

## InsightGenerationService

Responsibilities:
- summarize analytics
- generate executive insights
- explain trends

---

# Frontend Components

## PromptInputComponent

Responsibilities:
- accept user prompts
- support conversational queries

---

## ChartViewComponent

Responsibilities:
- render dynamic charts
- support:
  - bar
  - line
  - pie
  - table

---

## InsightPanelComponent

Responsibilities:
- display generated SQL
- display SemanticQuery
- display AI summaries

---

# Prompting Strategy

The AI prompt SHALL include:
- schema metadata
- business glossary
- semantic query examples
- allowed dimensions
- allowed metrics
- allowed filters

The AI SHALL return:
- SemanticQuery JSON only

The AI SHALL NOT return:
- executable SQL
- markdown
- explanations

---

# Semantic Metadata Files

Location:

.specify/specs/conversational-analytics/

Files:
- semantic-schema.json
- business-glossary.json
- semantic-query-examples.json

---

# Prompt Template Files

Location:

src/main/resources/prompts/

Files:
- semantic-query-generator.st
- chart-generator.st
- summary-generator.st

---

# Recommended Package Structure

com.hotbutton.analytics
    ├── controller
    ├── service
    ├── ai
    ├── dto
    ├── model
    ├── validation
    ├── repository
    ├── config
    └── prompts

---

# Recommended DTOs

- SemanticQuery
- AnalyticsRequest
- AnalyticsResponse
- QueryResult
- ChartMetadata
- FilterCriteria

---

# Visualization Rules

| Pattern | Visualization |
|---|---|
| category + aggregate | bar |
| time series + aggregate | line |
| proportions | pie |
| detailed rows | table |

---

# Security Strategy

## Allowed Operations
- SELECT
- GROUP BY
- ORDER BY
- LIMIT

---

## Blocked Operations
- UPDATE
- DELETE
- INSERT
- DROP
- ALTER
- TRUNCATE

---

# Phase-wise Implementation

## Phase 1 — Controlled Templates
- static query templates
- predefined analytics

---

## Phase 2 — Semantic Conversational Analytics
- SemanticQuery generation
- deterministic SQL builder
- dynamic chart generation

---

## Phase 3 — AI Visualization Intelligence
- advanced chart recommendation
- insight generation

---

## Phase 4 — Agentic Analytics
- planner agents
- orchestration agents
- autonomous analytics workflows

---

## Phase 5 — Predictive Analytics
- forecasting
- anomaly detection
- predictive insights

---

# Risks

## Risk 1 — Incorrect Semantic Extraction

Mitigation:
- semantic metadata
- business glossary
- semantic examples
- structured SemanticQuery validation

---

## Risk 2 — Unsafe SQL

Mitigation:
- deterministic SQL generation
- SQL validation
- read-only DB user

---

## Risk 3 — Slow Queries

Mitigation:
- query timeout
- LIMIT enforcement
- query optimization

---

## Risk 4 — Hallucinated Filters

Mitigation:
- semantic mapping rules
- filter resolver
- deterministic query building

---

# Success Criteria

- conversational analytics works
- OEM filters work correctly
- multi-filter queries work
- deterministic SQL generation works
- charts render dynamically
- security validation enforced
- response time < 5 sec
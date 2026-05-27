# Conversational Analytics Platform Specification

# Objective

Build an enterprise semantic conversational analytics platform
for automotive claims and escalations analytics.

The platform shall allow executives and analysts
to query analytics using natural language.

Examples:
- Show claims by severity
- Show claims by severity for Honda and Ford
- Show SLA breached escalations
- Show claim cost by OEM
- Show critical claims for Stellantis

---

# Problem Statement

Traditional dashboard systems require:
- manual navigation
- static filters
- predefined dashboards
- PowerBI interactions

The proposed solution shall provide:
- conversational analytics
- AI-powered semantic understanding
- dynamic chart generation
- governed analytics execution
- executive-friendly querying

---

# Users

## Primary Users
- Executives
- Quality Managers
- Plant Directors

## Secondary Users
- Analysts
- Engineering Teams

---

# Functional Requirements

## FR-1 Natural Language Analytics

The system shall:
- accept natural language prompts
- understand analytics intent
- understand business terminology
- extract dimensions and filters
- support conversational analytics

Example:
"Show claims by severity for Honda and Ford"

---

## FR-2 Semantic Query Interpretation

The AI system shall convert user prompts
into structured SemanticQuery objects.

Example:

User Prompt:
"Show claims by severity for Honda and Ford"

Semantic Query:

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

The AI system SHALL:
- understand semantics
- extract business meaning
- identify filters
- identify dimensions
- identify aggregations

The AI system SHALL NOT:
- directly execute SQL
- directly access database
- generate unsafe queries

---

## FR-3 Deterministic SQL Generation

The backend shall:
- generate PostgreSQL SQL deterministically
- build SQL using SemanticQuery
- apply filters safely
- apply aggregations safely
- enforce query governance

Example:

Semantic Query:

```json
{
  "metric": "count",
  "dimensions": ["severity"],
  "filters": {
    "oem_name": ["Honda", "Ford"]
  }
}
```

Generated SQL:

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

## FR-4 SQL Validation

The platform shall:
- validate generated SQL
- reject unsafe SQL
- restrict unauthorized tables
- restrict unauthorized columns
- enforce LIMIT clauses

Allowed:
- SELECT
- GROUP BY
- ORDER BY
- LIMIT

Blocked:
- UPDATE
- DELETE
- INSERT
- DROP
- ALTER
- TRUNCATE

---

## FR-5 Data Visualization

The platform shall:
- dynamically determine chart type
- generate chart metadata
- support bar charts
- support line charts
- support pie charts
- support table views

---

## FR-6 Insight Generation

The platform shall:
- summarize analytics results
- generate executive insights
- explain trends and anomalies

Example:
"Critical severity claims dominate Honda plants."

---

# Supported Analytics Domains

## Claims Analytics

Examples:
- claims by severity
- claims by OEM
- claims by defect category
- claims by plant
- claims by supplier responsibility

---

## Escalation Analytics

Examples:
- SLA breached escalations
- escalation aging
- critical escalations
- executive review escalations

---

## Financial Analytics

Examples:
- claim cost by OEM
- warranty cost trends
- chargeback analysis

---

# Semantic Query Model

The platform SHALL use SemanticQuery
as the intermediate analytics model.

Fields:

| Field | Description |
|---|---|
| domain | analytics domain |
| metric | aggregation metric |
| dimensions | grouping fields |
| filters | query filters |
| chartType | preferred visualization |
| limit | maximum rows |

---

# Semantic Mapping Rules

| User Phrase | Database Column |
|---|---|
| Honda | oem_name |
| Ford | oem_name |
| severity | severity |
| claim cost | claim_cost |
| SLA breached | sla_breached_flag |
| escalation aging | escalation_aging |

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
SQL Validation
↓
PostgreSQL Execution
↓
Chart Metadata Generation
↓
Insight Generation
↓
Frontend Rendering

---

# REST APIs

## POST /api/analytics/query

Request:

```json
{
  "prompt": "Show claims by severity for Honda"
}
```

Response:

```json
{
  "chartType": "bar",
  "labels": ["Critical", "Medium"],
  "values": [2, 1],
  "summary": "Critical claims dominate Honda claims.",
  "generatedSql": "SELECT ...",
  "semanticQuery": {
    "domain": "claims",
    "metric": "count"
  }
}
```

---

# AI Prompting Strategy

The AI prompt MUST include:
- schema metadata
- business glossary
- semantic examples
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

# Security Requirements

The platform MUST:
- allow SELECT only
- validate generated SQL
- enforce row limits
- apply query timeouts
- restrict tables and columns
- use read-only DB access

---

# Technology Stack

Frontend:
- Angular
- ngx-echarts

Backend:
- Spring Boot
- Spring AI

Database:
- PostgreSQL

LLM:
- Azure OpenAI
- Ollama

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

# Non Functional Requirements

| Requirement | Value |
|---|---|
| Response Time | < 5 sec |
| Query Timeout | 10 sec |
| Max Rows | 5000 |
| DB Access | Read-only |

---

# Future Enhancements

- Agentic analytics
- Predictive analytics
- RAG support
- Voice analytics
- Executive copilots
- Multi-agent orchestration
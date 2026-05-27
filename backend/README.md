# Conversational Analytics Backend

Spring Boot backend for natural-language analytics on automotive claims and escalations.

## Features

- `POST /api/analytics/query` — natural language prompt to chart-ready JSON
- Modular pipeline: intent → semantic context → SQL generation → validation → execution → chart metadata → insights
- PostgreSQL read-only queries with SELECT-only validation
- Spring AI + Ollama (`qwen2.5-coder`) with template/example fallback

## Prerequisites

- Java 17+
- Maven 3.9+
- PostgreSQL (production)
- [Ollama](https://ollama.com/) with `qwen2.5-coder` (optional, for AI SQL generation)

## Quick start

### 1. Database

```sql
CREATE DATABASE analytics;
CREATE USER analytics WITH PASSWORD 'analytics';
GRANT ALL PRIVILEGES ON DATABASE analytics TO analytics;
```

Apply schema from `src/main/resources/schema.sql` and load your data.

### 2. Ollama (optional)

```bash
ollama pull qwen2.5-coder
ollama serve
```

Set `analytics.sql-generation-mode=auto` (default) or `ollama` in `application.yml`.

Use `template` mode to run without Ollama (example matching + heuristics).

### 3. Run

```bash
cd backend
mvn spring-boot:run
```

### 4. Example request

```bash
curl -X POST http://localhost:8080/api/analytics/query \
  -H "Content-Type: application/json" \
  -d "{\"prompt\":\"Show claims by severity\"}"
```

Example response:

```json
{
  "chartType": "bar",
  "labels": ["Critical", "High", "Medium"],
  "values": [2, 2, 1],
  "summary": "Critical has the highest claim volume in this breakdown.",
  "generatedSql": "SELECT severity, COUNT(*) AS claim_count FROM part_claim GROUP BY severity ORDER BY claim_count DESC LIMIT 5000",
  "intent": "claims",
  "columns": ["severity", "claim_count"],
  "rows": [["Critical", 2], ["High", 2], ["Medium", 1]]
}
```

## Configuration

| Property | Default | Description |
|----------|---------|-------------|
| `analytics.sql-generation-mode` | `auto` | `auto`, `ollama`, or `template` |
| `analytics.query-timeout-seconds` | `10` | JDBC query timeout |
| `analytics.max-rows` | `5000` | Max rows returned |
| `spring.ai.ollama.chat.options.model` | `qwen2.5-coder` | LLM model |

## Metadata

Semantic metadata is loaded from `src/main/resources/metadata/`:

- `semantic-schema.json` — approved tables and columns
- `business-glossary.json` — business terms
- `query-examples.json` — few-shot SQL examples

## Tests

```bash
mvn test
```

Tests use in-memory H2 (PostgreSQL mode) with `TEMPLATE` SQL generation.

## Architecture

```
Prompt → IntentAnalysis → SemanticContext → SqlGeneration → SqlValidation
      → QueryExecution → ChartMetadata → InsightGeneration → Response
```

Approved tables: `part_claim`, `claim_escalation` (per constitution).

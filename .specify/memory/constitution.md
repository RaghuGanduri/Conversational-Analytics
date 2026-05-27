# Conversational Analytics Platform Constitution

## Purpose

Build a secure enterprise conversational analytics platform
for automotive claims and escalations analytics.

The platform enables executives and business users
to query analytics using natural language.

Example:
- "Show claims by severity"
- "Show claims for Honda"
- "Show SLA breached escalations"

---

# Core Principles

## Principle 1 — Security First

The platform MUST:
- allow ONLY read-only analytics queries
- never execute DML/DDL operations
- never allow UPDATE/DELETE/INSERT/DROP/ALTER/TRUNCATE

Allowed:
- SELECT
- GROUP BY
- ORDER BY
- LIMIT
- Aggregate functions

---

## Principle 2 — Semantic Understanding

The AI system MUST use:
- schema metadata
- business glossary
- entity mappings
- relationship metadata

The AI MUST NOT hallucinate table names or columns.

---

## Principle 3 — Controlled SQL Generation

Generated SQL MUST:
- use PostgreSQL syntax
- use only approved tables
- use only approved columns
- include LIMIT for large datasets
- avoid cartesian joins

---

## Principle 4 — Explainability

The system MUST return:
- generated SQL
- chart metadata
- business summary

The platform MUST remain explainable to executives.

---

## Principle 5 — Observability

The platform MUST log:
- prompts
- generated SQL
- execution time
- failures
- validation errors

---

## Principle 6 — Modularity

The system MUST separate:
- intent analysis
- SQL generation
- SQL validation
- chart generation
- summarization

---

## Principle 7 — Human Governance

The platform MUST:
- validate AI-generated SQL
- apply rule-based verification
- support human review

AI output MUST NEVER bypass validation.

---

# Approved Tables

- part_claim
- claim_escalation

---

# Approved Visualization Types

- bar
- line
- pie
- table

---

# Approved Models

Development:
- qwen2.5-coder
- llama3.1

Production:
- GPT-4.1
- Azure OpenAI

---

# Non Functional Requirements

- Response Time < 5 seconds
- Query Timeout < 10 seconds
- Max Result Rows = 5000
- Read-only DB access
- High availability support

---

# Future Extensions

- RAG support
- Predictive analytics
- Voice analytics
- Executive summaries
- Multi-agent orchestration
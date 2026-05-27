# Conversational Analytics Platform – Spec-Driven GenAI POC

## Overview

This project demonstrates a GenAI-powered Conversational Analytics Platform built using a Spec-Driven Development approach.

The solution enables business users and executives to interact with enterprise analytics using natural language instead of relying on pre-built dashboards or manually written SQL queries.

Example prompts:

- Show claims by severity
- Show claim cost by OEM
- Show SLA breached escalation count by manufacturer
- Show critical claims by OEM

The platform dynamically:
- Understands business intent
- Generates semantic queries
- Produces SQL automatically
- Executes analytics queries
- Renders dynamic charts and tables

---

# Business Objective

Traditional BI platforms require:
- prebuilt dashboards,
- BI development cycles,
- predefined KPIs,
- report maintenance.

This POC demonstrates an alternative conversational analytics model where:
- users ask business questions directly,
- GenAI interprets intent,
- analytics are generated dynamically.

The objective is to improve:
- executive visibility,
- operational agility,
- self-service analytics,
- real-time decision support.

---

# Solution Architecture

## Frontend
- Angular
- ECharts
- Dynamic chart rendering
- Conversational UI

## Backend
- Spring Boot
- Spring AI
- Semantic query engine
- SQL generation engine
- Analytics orchestration layer

## AI Layer
- Ollama
- Llama3/Mistral
- Semantic intent extraction

## Database
- PostgreSQL

---

# Spec-Driven Development Approach

This POC was developed using a Spec-Driven Development model.

Instead of directly writing implementation code, the solution was built by progressively refining:
- business intent,
- architecture specifications,
- implementation plans,
- executable tasks.

The project is structured around four foundational specification documents:

---

# 1. constitution.md

## Purpose

Defines the core engineering and architectural principles for the platform.

## Responsibilities

- Defines architectural standards
- Establishes coding principles
- Defines semantic governance
- Defines AI interaction boundaries
- Ensures consistent implementation patterns

## Example Topics

- Semantic-first architecture
- Backend-driven analytics validation
- Dynamic visualization principles
- Separation of AI inference vs deterministic logic
- Enterprise scalability considerations

---

# 2. spec.md

## Purpose

Captures detailed functional and technical specifications for the solution.

## Responsibilities

- Defines business capabilities
- Defines semantic query structure
- Defines analytics workflows
- Defines API contracts
- Defines visualization behavior
- Defines AI prompt engineering expectations

## Example Topics

- Conversational analytics flow
- SemanticQuery schema
- Dynamic SQL generation
- Chart metadata generation
- Business glossary integration
- Semantic schema definitions

---

# 3. plan.md

## Purpose

Defines the implementation strategy and execution roadmap.

## Responsibilities

- Breaks solution into phases
- Defines milestones
- Defines implementation sequence
- Defines integration strategy
- Defines technical dependencies

## Example Topics

- Backend implementation phases
- Angular UI implementation
- LLM integration strategy
- Semantic layer onboarding
- Visualization rollout
- Future enhancements roadmap

---

# 4. tasks.md

## Purpose

Tracks executable engineering tasks required for implementation.

## Responsibilities

- Defines development tasks
- Tracks feature completion
- Defines validation activities
- Defines testing activities
- Supports incremental delivery

## Example Topics

- Create SemanticQuery DTO
- Implement SQL builder
- Add semantic schema loader
- Implement chart metadata service
- Add Angular chart rendering
- Add LLM normalization logic

---

# Semantic Layer

The platform uses a semantic abstraction layer to improve GenAI grounding and business interpretation.

## Semantic Assets

### semantic-schema.json
Defines:
- domains
- tables
- dimensions
- metrics
- semantic metadata
- synonyms
- allowed values

### business-glossary.json
Defines:
- business vocabulary
- domain terminology
- semantic meaning mapping

### query-examples.json
Provides:
- example prompts
- expected semantic queries
- analytics intent guidance

---

# End-to-End Flow

```text
User Prompt
   ↓
LLM Semantic Interpretation
   ↓
Semantic Query
   ↓
SQL Generation
   ↓
SQL Validation
   ↓
Database Execution
   ↓
Chart Metadata Generation
   ↓
Dynamic Visualization

# AI-Assisted Spec-Driven Development

This POC was developed using an AI-assisted Spec-Driven Development approach leveraging:

- Visual Studio Code
- GitHub Copilot
- GenAI-assisted implementation workflows
- Semantic-first architecture design

---

# Development Methodology

Instead of traditional feature-by-feature coding, the solution was developed by progressively refining:

1. Architecture principles
2. Functional specifications
3. Implementation plans
4. Executable engineering tasks

The implementation lifecycle followed:

```text
Business Vision
      ↓
constitution.md
      ↓
spec.md
      ↓
plan.md
      ↓
tasks.md
      ↓
AI-assisted implementation
      ↓
Conversational Analytics Platform

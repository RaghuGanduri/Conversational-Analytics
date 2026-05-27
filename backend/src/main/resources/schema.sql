CREATE TABLE IF NOT EXISTS part_claim (
    claim_id BIGINT PRIMARY KEY,
    part_number VARCHAR(64) NOT NULL,
    severity VARCHAR(32) NOT NULL,
    oem_name VARCHAR(128) NOT NULL,
    claim_cost DECIMAL(12, 2) NOT NULL
);

CREATE TABLE IF NOT EXISTS claim_escalation (
    escalation_id BIGINT PRIMARY KEY,
    sla_breached_flag BOOLEAN NOT NULL
);

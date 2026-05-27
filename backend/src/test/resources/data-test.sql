DELETE FROM claim_escalation;
DELETE FROM part_claim;

INSERT INTO part_claim (claim_id, part_number, severity, oem_name, claim_cost) VALUES
  (1, 'PN-100', 'Critical', 'Honda', 15000.00),
  (2, 'PN-101', 'High', 'Honda', 8000.00),
  (3, 'PN-102', 'High', 'Ford', 6000.00),
  (4, 'PN-103', 'Medium', 'Ford', 2500.00),
  (5, 'PN-104', 'Critical', 'Toyota', 12000.00);

INSERT INTO claim_escalation (escalation_id, sla_breached_flag) VALUES
  (1001, true),
  (1002, false),
  (1003, true);

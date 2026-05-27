export interface AnalyticsResponse {}
export interface AnalyticsResponse {

  success: boolean;

  chartType: string;

  labels: string[];

  values: any[];

  summary: string;

  generatedSql: string;

  columns: string[];

  rows: any[];

  semanticQuery: any;

  rowCount: number;

  executionTimeMs: number;

  errorMessage?: string;
}
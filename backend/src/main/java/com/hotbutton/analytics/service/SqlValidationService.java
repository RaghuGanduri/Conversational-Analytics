package com.hotbutton.analytics.service;

import com.hotbutton.analytics.config.AnalyticsProperties;
import com.hotbutton.analytics.exception.AnalyticsException;
import com.hotbutton.analytics.dto.SqlValidationResult;
import java.util.HashSet;
import java.util.Locale;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.springframework.stereotype.Service;

@Service
public class SqlValidationService {

    private static final Set<String> FORBIDDEN_KEYWORDS = Set.of(
            "INSERT", "UPDATE", "DELETE", "DROP", "ALTER", "TRUNCATE", "CREATE", "GRANT", "REVOKE",
            "EXEC", "EXECUTE", "MERGE", "CALL", "COPY", "INTO");
    private static final Set<String> SQL_RESERVED_WORDS = Set.of(
            "SELECT", "FROM", "WHERE", "GROUP", "BY", "ORDER", "LIMIT", "AS", "AND", "OR", "NOT",
            "IN", "IS", "NULL", "DESC", "ASC", "TRUE", "FALSE", "LIKE", "ILIKE", "BETWEEN", "EXISTS",
            "ALL", "ANY", "UNION", "INTERSECT", "JOIN", "INNER", "LEFT", "RIGHT", "FULL", "OUTER",
            "ON", "CASE", "WHEN", "THEN", "ELSE", "END", "DISTINCT", "HAVING");
    private static final Set<String> SQL_FUNCTION_NAMES = Set.of(
            "COUNT", "SUM", "AVG", "MIN", "MAX", "COALESCE", "ROUND", "LOWER", "UPPER", "TRIM",
            "CAST", "DATE", "EXTRACT", "NOW");
    private static final Pattern TABLE_PATTERN =
            Pattern.compile("(?i)\\b(?:from|join)\\s+([a-z_][a-z0-9_]*)");
    private static final Pattern TABLE_ALIAS_PATTERN =
            Pattern.compile("(?i)\\b(?:from|join)\\s+([a-z_][a-z0-9_]*)(?:\\s+(?:as\\s+)?([a-z_][a-z0-9_]*))?\\b");
    private static final Pattern IDENTIFIER_PATTERN =
            Pattern.compile("(?i)\\b([a-z_][a-z0-9_]*(?:\\.[a-z_][a-z0-9_]*)?)\\b");
    private static final Pattern SELECT_ALIAS_PATTERN =
            Pattern.compile("(?i)\\bAS\\s+([a-z_][a-z0-9_]*)\\b");
    private static final Pattern TRAILING_ALIAS_PATTERN =
            Pattern.compile("(?i)([a-z_][a-z0-9_]*)\\s*$");
    private static final Pattern LIMIT_PATTERN = Pattern.compile("(?i)\\blimit\\s+(\\d+)");

    private final SemanticContextLoader semanticContextLoader;
    private final AnalyticsProperties properties;

    public SqlValidationService(SemanticContextLoader semanticContextLoader, AnalyticsProperties properties) {
        this.semanticContextLoader = semanticContextLoader;
        this.properties = properties;
    }

    public SqlValidationResult validate(String sql) {
        if (sql == null || sql.isBlank()) {
            throw new AnalyticsException("SQL_VALIDATION", "Generated SQL is empty");
        }

        String trimmed = sql.trim().replaceAll(";\\s*$", "");
        String upper = trimmed.toUpperCase(Locale.ROOT);

        for (String keyword : FORBIDDEN_KEYWORDS) {
            if (containsKeyword(upper, keyword)) {
                throw new AnalyticsException("SQL_VALIDATION", "Forbidden SQL operation: " + keyword);
            }
        }

        if (!upper.startsWith("SELECT")) {
            throw new AnalyticsException("SQL_VALIDATION", "Only SELECT queries are allowed");
        }

        if (upper.contains(";")) {
            throw new AnalyticsException("SQL_VALIDATION", "Multiple SQL statements are not allowed");
        }

        validateTables(trimmed);
        validateColumns(trimmed);

        String withLimit = ensureLimit(trimmed);
        return SqlValidationResult.builder()
        .valid(true)
        .sql(sql)
        .sanitizedSql(withLimit)
        .build();
    }

    private void validateTables(String sql) {
        Matcher matcher = TABLE_PATTERN.matcher(sql);
        Set<String> allowed = semanticContextLoader.getAllowedTables();
        boolean found = false;
        while (matcher.find()) {
            found = true;
            String table = matcher.group(1).toLowerCase(Locale.ROOT);
            if (!allowed.contains(table)) {
                throw new AnalyticsException(
                        "SQL_VALIDATION", "Unauthorized table: " + table);
            }
        }
        if (!found) {
            throw new AnalyticsException("SQL_VALIDATION", "Query must reference an approved table");
        }
    }

    private void validateColumns(String sql) {
        String lowerSql = sql.toLowerCase(Locale.ROOT);
        int fromIndex = lowerSql.indexOf(" from ");
        if (fromIndex < 0) {
            throw new AnalyticsException("SQL_VALIDATION", "Malformed SQL: missing FROM clause");
        }

        int whereIndex = lowerSql.indexOf(" where ", fromIndex);
        int groupByIndex = lowerSql.indexOf(" group by ", fromIndex);
        int orderByIndex = lowerSql.indexOf(" order by ", fromIndex);
        int limitIndex = lowerSql.indexOf(" limit ", fromIndex);

        int fromEnd = minPositive(whereIndex, groupByIndex, orderByIndex, limitIndex, sql.length());
        int whereEnd = minPositive(groupByIndex, orderByIndex, limitIndex, sql.length());
        int groupByEnd = minPositive(orderByIndex, limitIndex, sql.length());
        int orderByEnd = limitIndex >= 0 ? limitIndex : sql.length();

        String selectClause = sql.substring(0, fromIndex);
        String fromClause = sql.substring(fromIndex, fromEnd);
        String whereClause = whereIndex >= 0 ? sql.substring(whereIndex, whereEnd) : "";
        String groupByClause = groupByIndex >= 0 ? sql.substring(groupByIndex, groupByEnd) : "";
        String orderByClause = orderByIndex >= 0 ? sql.substring(orderByIndex, orderByEnd) : "";

        Set<String> aliases = extractAliases(selectClause, fromClause);

        validateClause(selectClause, aliases);
        validateClause(whereClause, aliases);
        validateClause(groupByClause, aliases);
        validateClause(orderByClause, aliases);
    }

    private Set<String> extractAliases(String selectClause, String fromClause) {
        Set<String> aliases = new HashSet<>();
        Matcher aliasMatcher = SELECT_ALIAS_PATTERN.matcher(selectClause);
        while (aliasMatcher.find()) {
            aliases.add(aliasMatcher.group(1).toLowerCase(Locale.ROOT));
        }

        String normalizedSelect = selectClause.replaceFirst("(?i)^SELECT\\s+", "");
        String[] selectParts = normalizedSelect.split(",");
        for (String part : selectParts) {
            String trimmed = part.trim();
            if (!trimmed.contains(" ")) {
                continue;
            }
            Matcher trailingAlias = TRAILING_ALIAS_PATTERN.matcher(trimmed);
            if (trailingAlias.find()) {
                String alias = trailingAlias.group(1).toLowerCase(Locale.ROOT);
                if (!isSqlReservedWord(alias) && !isSqlFunction(alias) && !trimmed.equalsIgnoreCase(alias)) {
                    aliases.add(alias);
                }
            }
        }

        Matcher tableAliasMatcher = TABLE_ALIAS_PATTERN.matcher(fromClause);
        while (tableAliasMatcher.find()) {
            String alias = tableAliasMatcher.group(2);
            if (alias != null) {
                aliases.add(alias.toLowerCase(Locale.ROOT));
            }
        }
        return aliases;
    }

    private void validateClause(String clause, Set<String> aliases) {
        if (clause == null || clause.isBlank()) {
            return;
        }
        String sanitizedClause = removeQuotedLiterals(clause);
        Matcher matcher = IDENTIFIER_PATTERN.matcher(sanitizedClause);
        while (matcher.find()) {
            String token = matcher.group(1).toLowerCase(Locale.ROOT);
            if (token.contains(".")) {
                String[] parts = token.split("\\.");
                String qualifier = parts[0];
                String column = parts[parts.length - 1];
                if (!semanticContextLoader.getAllowedTables().contains(qualifier) && !aliases.contains(qualifier)) {
                    throw new AnalyticsException("SQL_VALIDATION", "Unauthorized column qualifier: " + qualifier);
                }
                validateIdentifier(column, aliases);
            } else {
                validateIdentifier(token, aliases);
            }
        }
    }

    private String removeQuotedLiterals(String clause) {
        return clause.replaceAll("(?s)'(?:''|[^'])*'", " ");
    }

    private void validateIdentifier(String identifier, Set<String> aliases) {
        if (identifier.equals("*") || isSqlReservedWord(identifier) || isSqlFunction(identifier)
                || isNumeric(identifier) || isBooleanLiteral(identifier)) {
            return;
        }
        if (aliases.contains(identifier)
                || semanticContextLoader.getAllowedColumns().contains(identifier)
                || semanticContextLoader.getAllowedTables().contains(identifier)) {
            return;
        }
        throw new AnalyticsException("SQL_VALIDATION", "Unauthorized column: " + identifier);
    }

    private boolean isSqlReservedWord(String token) {
        return SQL_RESERVED_WORDS.contains(token.toUpperCase(Locale.ROOT));
    }

    private boolean isSqlFunction(String token) {
        return SQL_FUNCTION_NAMES.contains(token.toUpperCase(Locale.ROOT));
    }

    private boolean isNumeric(String token) {
        return token.matches("\\d+");
    }

    private boolean isBooleanLiteral(String token) {
        return "true".equals(token) || "false".equals(token);
    }

    private int minPositive(int... values) {
        int min = Integer.MAX_VALUE;
        for (int value : values) {
            if (value >= 0 && value < min) {
                min = value;
            }
        }
        return min == Integer.MAX_VALUE ? -1 : min;
    }

    private String ensureLimit(String sql) {
        Matcher limitMatcher = LIMIT_PATTERN.matcher(sql);
        if (limitMatcher.find()) {
            int limit = Integer.parseInt(limitMatcher.group(1));
            if (limit > properties.getMaxRows()) {
                throw new AnalyticsException(
                        "SQL_VALIDATION", "LIMIT exceeds maximum allowed rows: " + properties.getMaxRows());
            }
            return sql;
        }
        return sql + " LIMIT " + properties.getDefaultLimit();
    }

    private boolean containsKeyword(String sql, String keyword) {
        String sanitized = removeQuotedLiterals(sql);
        return Pattern.compile("\\b" + keyword + "\\b").matcher(sanitized).find();
    }
}

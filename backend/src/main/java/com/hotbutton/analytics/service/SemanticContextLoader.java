package com.hotbutton.analytics.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hotbutton.analytics.metadata.BusinessGlossary;
import com.hotbutton.analytics.metadata.SemanticQueryExample;
import com.hotbutton.analytics.metadata.SemanticSchema;
import com.hotbutton.analytics.metadata.SemanticTable;
import jakarta.annotation.PostConstruct;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

@Service
public class SemanticContextLoader {

    private static final String SCHEMA_PATH = "metadata/semantic-schema.json";
    private static final String GLOSSARY_PATH = "metadata/business-glossary.json";
    private static final String EXAMPLES_PATH = "metadata/query-examples.json";

    private final ObjectMapper objectMapper;
    private SemanticSchema schema;
    private BusinessGlossary glossary;
    private List<SemanticQueryExample> examples;
    private Set<String> allowedColumns;
    private Set<String> allowedTables;

    public SemanticContextLoader(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @PostConstruct
    public void loadMetadata() throws IOException {
        this.schema = load(SCHEMA_PATH, SemanticSchema.class);
        this.glossary = load(GLOSSARY_PATH, BusinessGlossary.class);
        this.examples = Arrays.asList(load(EXAMPLES_PATH, SemanticQueryExample[].class));
        this.allowedTables = schema.getTables().stream()
                .map(SemanticTable::getTableName)
                .collect(Collectors.toUnmodifiableSet());
        this.allowedColumns = schema.getTables().stream()
                .flatMap(table -> table.getColumns().stream().map(column -> column.getName().toLowerCase()))
                .collect(Collectors.toUnmodifiableSet());
    }

    public SemanticSchema getSchema() {
        return schema;
    }

    public BusinessGlossary getGlossary() {
        return glossary;
    }

    public List<SemanticQueryExample> getExamples() {
        return examples;
    }

    public SemanticSchema loadSchema() throws IOException {
        if (schema == null) {
            loadMetadata();
        }
        return schema;
    }

    public BusinessGlossary loadGlossary() throws IOException {
        if (glossary == null) {
            loadMetadata();
        }
        return glossary;
    }

    public List<SemanticQueryExample> loadExamples() throws IOException {
        if (examples == null) {
            loadMetadata();
        }
        return examples;
    }

    public Set<String> getAllowedColumns() {
        return allowedColumns;
    }

    public Set<String> getAllowedTables() {
        return allowedTables;
    }

    private <T> T load(String path, Class<T> type) throws IOException {
        ClassPathResource resource = new ClassPathResource(path);
        try (InputStream input = resource.getInputStream()) {
            return objectMapper.readValue(input, type);
        }
    }
}

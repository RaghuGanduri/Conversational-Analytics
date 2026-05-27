package com.hotbutton.analytics.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hotbutton.analytics.metadata.BusinessGlossary;
import com.hotbutton.analytics.metadata.SemanticQueryExample;
import com.hotbutton.analytics.metadata.SemanticSchema;
import java.io.IOException;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class SemanticContextLoaderTest {

    private SemanticContextLoader loader;

    @BeforeEach
    void setUp() {
        loader = new SemanticContextLoader(new ObjectMapper());
    }

    @Test
    void loadsSemanticSchema() throws IOException {
        loader.loadMetadata();
        SemanticSchema schema = loader.loadSchema();

        assertThat(schema).isNotNull();
        assertThat(schema.database()).isEqualTo("PostgreSQL");
        assertThat(schema.tables()).hasSize(2);
        assertThat(schema.tables().get(0).tableName()).isEqualTo("part_claim");
        assertThat(schema.tables().get(0).columns()).hasSize(5);
    }

    @Test
    void loadsBusinessGlossary() throws IOException {
        loader.loadMetadata();
        BusinessGlossary glossary = loader.loadGlossary();

        assertThat(glossary).isNotNull();
        assertThat(glossary.businessTerms()).hasSize(3);
        assertThat(glossary.businessTerms().get(0).term()).isEqualTo("hot button indicator");
        assertThat(glossary.businessTerms().get(0).meaning())
                .contains("critical claims and escalations");
    }

    @Test
    void loadsQueryExamples() throws IOException {
        loader.loadMetadata();
        List<SemanticQueryExample> examples = loader.loadExamples();

        assertThat(examples).hasSize(4);
        assertThat(examples.get(0).question()).isEqualTo("Show claims by severity");
        assertThat(examples.get(0).sql()).contains("SELECT", "part_claim");
    }
}

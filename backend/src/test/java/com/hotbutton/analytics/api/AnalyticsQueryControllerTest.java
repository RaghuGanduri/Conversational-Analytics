package com.hotbutton.analytics.api;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class AnalyticsQueryControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void queryClaimsBySeverity() throws Exception {
        mockMvc.perform(post("/api/analytics/query")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"prompt\":\"Show claims by severity\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.chartType").value("bar"))
                .andExpect(jsonPath("$.labels", hasSize(3)))
                .andExpect(jsonPath("$.values", hasSize(3)))
                .andExpect(jsonPath("$.generatedSql", containsString("part_claim")))
                .andExpect(jsonPath("$.summary").isNotEmpty());
    }

    @Test
    void queryClaimsForHonda() throws Exception {
        mockMvc.perform(post("/api/analytics/query")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"prompt\":\"Show claims for Honda\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.rows", hasSize(2)))
                .andExpect(jsonPath("$.generatedSql", containsString("Honda")));
    }
}

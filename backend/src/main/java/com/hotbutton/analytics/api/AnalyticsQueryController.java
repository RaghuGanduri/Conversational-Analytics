package com.hotbutton.analytics.api;

import com.hotbutton.analytics.dto.AnalyticsResponse;
import com.hotbutton.analytics.dto.AnalyticsRequest;
import com.hotbutton.analytics.service.AnalyticsOrchestratorService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.CrossOrigin;

@CrossOrigin(origins = "http://localhost:4200")
@RestController
@RequestMapping("/api/analytics")
public class AnalyticsQueryController {

    private final AnalyticsOrchestratorService orchestratorService;

    public AnalyticsQueryController(AnalyticsOrchestratorService orchestratorService) {
        this.orchestratorService = orchestratorService;
    }

    @PostMapping("/query")
    public AnalyticsResponse query(@Valid @RequestBody AnalyticsRequest request) {
        return orchestratorService.process(request.getPrompt());
    }
}

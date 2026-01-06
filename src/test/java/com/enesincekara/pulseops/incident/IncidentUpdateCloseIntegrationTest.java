package com.enesincekara.pulseops.incident;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;

import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@Testcontainers
@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class IncidentUpdateCloseIntegrationTest {

    @Container
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:16-alpine");

    @DynamicPropertySource
    static void props(DynamicPropertyRegistry r) {
        r.add("spring.datasource.url", postgres::getJdbcUrl);
        r.add("spring.datasource.username", postgres::getUsername);
        r.add("spring.datasource.password", postgres::getPassword);

        // Flyway çalışsın
        r.add("spring.flyway.enabled", () -> "true");
    }

    @Autowired MockMvc mvc;
    @Autowired ObjectMapper om;

    private JsonNode createIncident() throws Exception {
        String body = """
                {
                  "title": "test",
                  "description": "test",
                  "severity": "LOW"
                }
                """;

        MvcResult res = mvc.perform(post("/api/v1/incidents")
                        .contentType(APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isCreated())
                .andReturn();

        return om.readTree(res.getResponse().getContentAsString());
    }

    @Test
    void update_then_close_happy_path() throws Exception {
        JsonNode created = createIncident();
        String id = created.get("id").asText();
        long version = created.get("version").asLong();

        String patchBody = """
                {
                  "title": "updated",
                  "version": %d
                }
                """.formatted(version);

        MvcResult updatedRes = mvc.perform(patch("/api/v1/incidents/{id}", id)
                        .contentType(APPLICATION_JSON)
                        .content(patchBody))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("updated"))
                .andReturn();

        JsonNode updated = om.readTree(updatedRes.getResponse().getContentAsString());
        long newVersion = updated.get("version").asLong();

        // CLOSE
        String closeBody = """
                {
                  "version": %d
                }
                """.formatted(newVersion);

        mvc.perform(post("/api/v1/incidents/{id}/close", id)
                        .contentType(APPLICATION_JSON)
                        .content(closeBody))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("CLOSED"));
    }

    @Test
    void update_with_wrong_version_returns_409() throws Exception {
        JsonNode created = createIncident();
        String id = created.get("id").asText();

        String patchBody = """
                {
                  "title": "x",
                  "version": 999
                }
                """;

        mvc.perform(patch("/api/v1/incidents/{id}", id)
                        .contentType(APPLICATION_JSON)
                        .content(patchBody))
                .andExpect(status().isConflict());
    }

    @Test
    void update_with_empty_body_returns_400() throws Exception {
        JsonNode created = createIncident();
        String id = created.get("id").asText();

        mvc.perform(patch("/api/v1/incidents/{id}", id)
                        .contentType(APPLICATION_JSON)
                        .content("{}"))
                .andExpect(status().isBadRequest());
    }
}

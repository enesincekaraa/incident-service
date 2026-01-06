package com.enesincekara.pulseops.incident.api.dto;

import com.enesincekara.pulseops.incident.domain.enums.IncidentSeverity;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record IncidentUpdateRequest(

        @Schema(example = "Checkout latency spike")
        @Size(max = 120, message = "title max 120")
        @Pattern(regexp = ".*\\S.*", message = "title must not be blank")
        String title,
        @Schema(example = "p95 latency went above 2s")
        @Size(max = 2000, message = "description max 2000")
        @Pattern(regexp = "(?s).*\\S.*", message = "description must not be blank")
        String description,
        IncidentSeverity severity,
        @Schema(example = "0", description = "Optimistic locking version")
        Long version
) {
    @AssertTrue(message = "At least one field (title, description, severity) must be provided")
    public boolean isAnyFieldProvided() {
        return title != null || description != null || severity != null;
    }
}

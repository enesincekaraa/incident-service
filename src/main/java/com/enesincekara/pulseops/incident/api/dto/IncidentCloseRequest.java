package com.enesincekara.pulseops.incident.api.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import org.springframework.scheduling.annotation.Scheduled;

public record IncidentCloseRequest(
        @NotNull
        @Schema(example = "0",description = "Optimistic locking version")
        Long version
) {
}

package com.enesincekara.pulseops.incident.api.dto;

import com.enesincekara.pulseops.incident.domain.enums.IncidentSeverity;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record CreateIncidentRequest(
        @NotBlank @Size(max = 200) String title,
        @Size(max = 5000) String description,
        @NotNull IncidentSeverity severity
        ) {
}

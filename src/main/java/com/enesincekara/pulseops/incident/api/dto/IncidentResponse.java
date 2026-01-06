package com.enesincekara.pulseops.incident.api.dto;

import com.enesincekara.pulseops.incident.domain.enums.IncidentSeverity;
import com.enesincekara.pulseops.incident.domain.enums.IncidentStatus;

import java.time.OffsetDateTime;
import java.util.UUID;

public record IncidentResponse(
        UUID id,
        String title,
        String description,
        IncidentSeverity severity,
        IncidentStatus status,
        OffsetDateTime createdAt,
        OffsetDateTime updatedAt,
        long version

) {
}

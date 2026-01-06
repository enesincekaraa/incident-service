package com.enesincekara.pulseops.incident.api.mapper;

import com.enesincekara.pulseops.incident.api.dto.IncidentResponse;
import com.enesincekara.pulseops.incident.domain.model.Incident;
import org.springframework.stereotype.Component;


@Component
public  class IncidentMapper {
    public IncidentResponse toResponse(Incident incident) {
        return new IncidentResponse(
                incident.getId(),
                incident.getTitle(),
                incident.getDescription(),
                incident.getSeverity(),
                incident.getStatus(),
                incident.getCreatedAt(),
                incident.getUpdatedAt(),
                incident.getVersion()
        );
    }
}

package com.enesincekara.pulseops.incident.api.exception;

import java.util.UUID;

public class IncidentNotFoundException extends RuntimeException {
    public IncidentNotFoundException(UUID id) {
        super("Incident with id " + id + " not found");
    }
}

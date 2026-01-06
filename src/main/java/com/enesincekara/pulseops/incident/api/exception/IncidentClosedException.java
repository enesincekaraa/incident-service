package com.enesincekara.pulseops.incident.api.exception;

import java.util.UUID;

public class IncidentClosedException extends RuntimeException {
    public IncidentClosedException(UUID id) {
        super("Incident is CLOSED and cannot be modified. id=" + id);
    }
}

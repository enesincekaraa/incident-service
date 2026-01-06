package com.enesincekara.pulseops.incident.api.exception;

import java.util.UUID;

public class IncidentVersionMismatchException extends RuntimeException {
    public IncidentVersionMismatchException(UUID id, Long expected, Long actual) {
        super("Incident version mismatch. id=" + id + ", expected=" + expected + ", actual=" + actual);
    }
}

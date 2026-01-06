package com.enesincekara.pulseops.incident.service;

import com.enesincekara.pulseops.incident.api.dto.CreateIncidentRequest;
import com.enesincekara.pulseops.incident.api.dto.IncidentCloseRequest;
import com.enesincekara.pulseops.incident.api.dto.IncidentUpdateRequest;
import com.enesincekara.pulseops.incident.api.exception.IncidentClosedException;
import com.enesincekara.pulseops.incident.api.exception.IncidentNotFoundException;
import com.enesincekara.pulseops.incident.api.exception.IncidentVersionMismatchException;
import com.enesincekara.pulseops.incident.domain.model.Incident;
import com.enesincekara.pulseops.incident.domain.enums.IncidentStatus;
import com.enesincekara.pulseops.incident.repository.IncidentRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
public class IncidentService {

    private final IncidentRepository repository;

    public IncidentService(IncidentRepository incidentRepository) {
        this.repository = incidentRepository;
    }

    @Transactional
    public Incident create(CreateIncidentRequest req) {
        Incident incident = new Incident();
        incident.setTitle(req.title());
        incident.setDescription(req.description());
        incident.setSeverity(req.severity());
        incident.setStatus(IncidentStatus.OPEN);
        return repository.save(incident);
    }

    @Transactional(readOnly = true)
    public Incident getById(UUID id) {
        return repository.findById(id).orElseThrow(()->
            new IncidentNotFoundException(id)
        );



    }

    @Transactional
    public Incident update(UUID id, IncidentUpdateRequest req) {
        Incident incident = repository.findById(id).orElseThrow(
                () -> new IncidentNotFoundException(id)
        );

        if (req.version() != null && !req.version().equals(incident.getVersion())) {
            throw new IncidentVersionMismatchException(id, incident.getVersion(),req.version());
        }
        if (incident.getStatus() == IncidentStatus.CLOSED) {
            throw new IncidentClosedException(id);
        }
        if (req.title() != null) {
            incident.setTitle(req.title());
        }
        if (req.description() != null) {
            incident.setDescription(req.description());
        }

        if (req.severity() != null) {
            incident.setSeverity(req.severity());
        }

        return repository.save(incident);
    }

    @Transactional
    public Incident close(UUID id, IncidentCloseRequest req) {
        Incident incident = repository.findById(id)
                .orElseThrow(() -> new IncidentNotFoundException(id));

        if (!req.version().equals(incident.getVersion())) {
            throw new IncidentVersionMismatchException(id, incident.getVersion(), req.version());
        }

        if (incident.getStatus() == IncidentStatus.CLOSED) {
            throw new IncidentClosedException(id);
        }

        incident.close();
        return repository.save(incident);
    }
}

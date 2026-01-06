package com.enesincekara.pulseops.incident.repository;

import com.enesincekara.pulseops.incident.domain.model.Incident;
import com.enesincekara.pulseops.incident.domain.enums.IncidentSeverity;
import com.enesincekara.pulseops.incident.domain.enums.IncidentStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface IncidentRepository extends JpaRepository<Incident, UUID> {
    Optional<Incident> findById(UUID id);
    Page<Incident> findByStatusAndSeverity(IncidentStatus status, IncidentSeverity severity, Pageable pageable);
    Page<Incident> findByStatus(IncidentStatus status, Pageable pageable);
    Page<Incident> findBySeverity(IncidentSeverity severity, Pageable pageable);
    Page<?> findAll(Specification<Incident> incidentSpecification, Pageable pageable);
}

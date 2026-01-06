package com.enesincekara.pulseops.incident.api.controller;

import com.enesincekara.pulseops.incident.api.dto.*;
import com.enesincekara.pulseops.incident.api.mapper.IncidentMapper;
import com.enesincekara.pulseops.incident.domain.model.Incident;
import com.enesincekara.pulseops.incident.domain.enums.IncidentSeverity;
import com.enesincekara.pulseops.incident.domain.enums.IncidentStatus;
import com.enesincekara.pulseops.incident.repository.IncidentQueryService;
import com.enesincekara.pulseops.incident.service.IncidentService;
import jakarta.validation.Valid;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Pageable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.time.OffsetDateTime;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/incidents")
public class IncidentController {

    private final IncidentService service;
    private final IncidentMapper mapper;
    private final IncidentQueryService queryService;

    public IncidentController(IncidentService service, IncidentMapper mapper, IncidentQueryService queryService) {
        this.service = service;
        this.mapper = mapper;
        this.queryService = queryService;
    }

    @GetMapping("/{id}")
    public IncidentResponse findById(@PathVariable UUID id) {
        Incident incident = service.getById(id);
        return mapper.toResponse(incident);
    }
    @GetMapping
    public PageResponse<IncidentResponse> search(
            @RequestParam(required = false) IncidentStatus status,
            @RequestParam(required = false) IncidentSeverity severity,
            @RequestParam(required = false) String q,
            @RequestParam(required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) OffsetDateTime from,
            @RequestParam(required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) OffsetDateTime to,
            @ParameterObject Pageable pageable
    ) {
        return queryService.findAll(status, severity, q, from, to, pageable);
    }


    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public IncidentResponse create(@Valid @RequestBody CreateIncidentRequest request) {
        Incident incident = service.create(request);
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


    @PatchMapping("/{id}")
    public IncidentResponse update(@PathVariable UUID id, @Valid @RequestBody IncidentUpdateRequest req) {
        return mapper.toResponse(service.update(id, req));
    }

    @PostMapping("/{id}/close")
    public IncidentResponse close(@PathVariable UUID id, @Valid @RequestBody IncidentCloseRequest req) {
        return mapper.toResponse(service.close(id, req));
    }

}

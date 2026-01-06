package com.enesincekara.pulseops.incident.repository;

import com.enesincekara.pulseops.incident.api.mapper.IncidentMapper;
import com.enesincekara.pulseops.incident.api.dto.IncidentResponse;
import com.enesincekara.pulseops.incident.api.dto.PageResponse;
import com.enesincekara.pulseops.incident.domain.model.Incident;
import com.enesincekara.pulseops.incident.domain.enums.IncidentSeverity;
import com.enesincekara.pulseops.incident.domain.enums.IncidentStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;
import java.util.stream.Collectors;

@Service
public class IncidentQueryService {
    private final IncidentRepository repository;

    private final IncidentMapper mapper;

    public IncidentQueryService(IncidentRepository repository, IncidentMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    public PageResponse<IncidentResponse> findAll(
            IncidentStatus status,
            IncidentSeverity severity,
            String q,
            OffsetDateTime from,
            OffsetDateTime to,
            Pageable pageable
    ) {
        Page<?> page = repository.findAll(
                IncidentSpecifications.byFilters(status, severity, q, from, to),
                pageable
        );

        var items = page.getContent().stream()
                .map(it -> (Incident) it)
                .map(mapper::toResponse)
                .collect(Collectors.toList());

        String sort = pageable.getSort().isUnsorted() ? "" : pageable.getSort().toString();

        return new PageResponse<>(
                items,
                new PageResponse.PageMeta(
                        page.getNumber(),
                        page.getSize(),
                        page.getTotalElements(),
                        page.getTotalPages(),
                        page.isFirst(),
                        page.isLast(),
                        sort
                )
        );
    }
}

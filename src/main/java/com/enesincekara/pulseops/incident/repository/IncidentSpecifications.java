package com.enesincekara.pulseops.incident.repository;

import com.enesincekara.pulseops.incident.domain.model.Incident;
import com.enesincekara.pulseops.incident.domain.enums.IncidentSeverity;
import com.enesincekara.pulseops.incident.domain.enums.IncidentStatus;
import org.springframework.data.jpa.domain.Specification;

import java.time.OffsetDateTime;


public final class IncidentSpecifications {
    private IncidentSpecifications() {}

    public static Specification<Incident> byFilters(
            IncidentStatus status,
            IncidentSeverity severity,
            String q,
            OffsetDateTime from,
            OffsetDateTime to
            ) {
        return (root, query, cb)
                -> {
            var predicate = cb.conjunction();

            if (status != null) {
                predicate.getExpressions().add(cb.equal(root.get("status"), status));
            }
            if (severity != null) {
                predicate.getExpressions().add(cb.equal(root.get("severity"), severity));
            }
            if (q != null && !q.isBlank()) {
                String like =  "%" + q.trim().toLowerCase() + "%";
                var titleLike = cb.like(root.get("title"), like);
                var descriptionLike = cb.like(root.get("description"), like);
                predicate.getExpressions().add(cb.or(titleLike, descriptionLike));
            }
            if (from != null) {
                predicate.getExpressions().add(cb.greaterThanOrEqualTo(root.get("created"), from));
            }
            if (to != null) {
                predicate.getExpressions().add(cb.lessThanOrEqualTo(root.get("created"), to));
            }
            return predicate;
        };
    }
}

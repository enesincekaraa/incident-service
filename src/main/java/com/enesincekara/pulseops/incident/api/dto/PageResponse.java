package com.enesincekara.pulseops.incident.api.dto;

import java.util.List;

public record PageResponse<T>(
        List<T> items,
        PageMeta meta
) {
    public record PageMeta(
            int page,
            int size,
            long totalElements,
            int totalPages,
            boolean first,
            boolean last,
            String sort
    ) {}
}

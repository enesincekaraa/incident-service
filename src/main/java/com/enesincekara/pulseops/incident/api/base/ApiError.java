package com.enesincekara.pulseops.incident.api.base;

import java.time.OffsetDateTime;

public record ApiError(
        OffsetDateTime time,
        int status,
        String error,
        String message
) {


}

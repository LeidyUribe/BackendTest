package com.test.franchise.exceptions;

import java.time.Instant;
import java.util.List;

public record ErrorResponse(
        Instant timestamp,
        int status,
        String error,
        String path,
        List<String> details
) {}
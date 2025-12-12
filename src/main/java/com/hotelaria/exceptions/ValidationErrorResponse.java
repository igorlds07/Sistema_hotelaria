package com.hotelaria.exceptions;

import java.time.LocalDateTime;
import java.util.List;

public record ValidationErrorResponse(
        int status,
        String error,
        List<String> errors,
        String path,
        LocalDateTime timestamp
) {
}

package com.hotelaria.exceptions;

import java.time.LocalDateTime;

public record ErrorResponse(
        int status,
        String error,
        String menssagem,
        String path,
        LocalDateTime timestamp

) {
}

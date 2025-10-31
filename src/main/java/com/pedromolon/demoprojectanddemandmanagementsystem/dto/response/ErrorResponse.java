package com.pedromolon.demoprojectanddemandmanagementsystem.dto.response;

import java.time.LocalDateTime;
import java.util.List;

public record ErrorResponse(
        LocalDateTime timestamp,
        int status,
        String error,
        String message,
        String path,
        List<String> details
) {
    public ErrorResponse(int timestamp, String status, String error, String message, List<String> path) {
        this(timestamp, status, error, message, path, null);
    }
}

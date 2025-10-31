package com.pedromolon.demoprojectanddemandmanagementsystem.dto.request;

import com.pedromolon.demoprojectanddemandmanagementsystem.entity.enums.Status;

public record TaskStatusRequest(
        Status status
) {
}

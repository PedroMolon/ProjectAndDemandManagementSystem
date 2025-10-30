package com.pedromolon.demoprojectanddemandmanagementsystem.dto.response;

import com.pedromolon.demoprojectanddemandmanagementsystem.entity.enums.Priority;
import com.pedromolon.demoprojectanddemandmanagementsystem.entity.enums.Status;
import lombok.Builder;

import java.time.LocalDate;

@Builder
public record TaskResponse(
        Long id,
        Long projectId,
        String title,
        String description,
        Status status,
        Priority priority,
        LocalDate dueDate
) {
}

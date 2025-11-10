package com.pedromolon.demoprojectanddemandmanagementsystem.dto.response;

import lombok.Builder;

import java.time.LocalDate;

@Builder
public record ProjectResponse(
        Long id,
        String name,
        String description,
        LocalDate startDate,
        LocalDate endDate,
        Long userId,
        String userName
) {
}

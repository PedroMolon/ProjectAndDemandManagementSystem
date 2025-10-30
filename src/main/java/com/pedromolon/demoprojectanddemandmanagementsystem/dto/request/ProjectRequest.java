package com.pedromolon.demoprojectanddemandmanagementsystem.dto.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Builder;

import java.time.LocalDate;

@Builder
public record ProjectRequest(
        @NotBlank(message = "Project name cannot be blank")
        @Size(min = 3, max = 100, message = "Project name must be between 3 and 100 characters")
        String name,

        String description,

        @JsonFormat(pattern = "dd/MM/yyyy")
        LocalDate startDate,

        @JsonFormat(pattern = "dd/MM/yyyy")
        LocalDate endDate
) {
}

package com.pedromolon.demoprojectanddemandmanagementsystem.dto.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.pedromolon.demoprojectanddemandmanagementsystem.entity.enums.Priority;
import com.pedromolon.demoprojectanddemandmanagementsystem.entity.enums.Status;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Builder;

import java.time.LocalDate;

@Builder
public record TaskRequest(
        @NotNull(message = "Project id cannot be null")
        Long projectId,

        @NotBlank(message = "Task title cannot be blank")
        @Size(min = 5, max = 150, message = "Task title must be between 5 and 150 characters")
        String title,

        String description,

        @NotNull(message = "Task status cannot be null")
        Status status,

        @NotNull(message = "Task priority cannot be null")
        Priority priority,

        @JsonFormat(pattern = "dd/MM/yyyy")
        LocalDate dueDate
) {
}

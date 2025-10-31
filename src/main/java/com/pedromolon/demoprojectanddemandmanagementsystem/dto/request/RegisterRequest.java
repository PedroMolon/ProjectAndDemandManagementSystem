package com.pedromolon.demoprojectanddemandmanagementsystem.dto.request;

import com.pedromolon.demoprojectanddemandmanagementsystem.entity.enums.Role;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record RegisterRequest(
        @NotNull(message = "Name cannot be null")
        String name,

        @NotNull(message = "Email cannot be null")
        @Email(message = "Email must be valid")
        String email,

        @NotNull(message = "Password cannot be null")
        @Size(min = 8, message = "Password must be at least 8 characters")
        String password,

        Role role
) {
}

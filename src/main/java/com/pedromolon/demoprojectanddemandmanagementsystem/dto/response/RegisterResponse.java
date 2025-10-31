package com.pedromolon.demoprojectanddemandmanagementsystem.dto.response;

import lombok.Builder;

@Builder
public record RegisterResponse(
        String name,
        String email
) {
}

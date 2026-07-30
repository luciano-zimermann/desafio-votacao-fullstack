package com.lucianozimermann.desafiovotacaofullstack.dto.response;

import lombok.Builder;

import java.time.Instant;
import java.util.List;

@Builder
public record ErrorResponse(
        Instant timestamp,
        Integer status,
        String error,
        String path,
        List<String> errors
) {
}
package com.lucianozimermann.desafiovotacaofullstack.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;

@Builder
public record AgendaRequestDTO(

        @NotBlank
        String name,

        String description

) {
}
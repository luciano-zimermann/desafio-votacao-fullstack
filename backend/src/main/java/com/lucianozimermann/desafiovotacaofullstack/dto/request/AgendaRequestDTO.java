package com.lucianozimermann.desafiovotacaofullstack.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;

@Builder
public record AgendaRequestDTO(

        @NotBlank(message = "Nome da pauta é obrigatório")
        String name,

        String description
) {
}
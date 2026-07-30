package com.lucianozimermann.desafiovotacaofullstack.dto.response;

import lombok.Builder;

@Builder
public record AgendaResponseDTO(

        Long id,
        String name,
        String description

) {
}
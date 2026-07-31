package com.lucianozimermann.desafiovotacaofullstack.dto.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Builder;

@Builder
public record SessionRequestDTO(

        @NotNull(message = "Id da pauta é obrigatório")
        Long agendaId,

        @Positive(message = "Duração deve ser maior que zero")
        Integer duration
) {
}
package com.lucianozimermann.desafiovotacaofullstack.dto.request;

import com.lucianozimermann.desafiovotacaofullstack.enums.VoteValue;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;

@Builder
public record VoteRequestDTO(

        @NotNull(message = "Id da sessão é obrigatório")
        Long sessionId,

        @NotNull(message = "Id do associado é obrigatório")
        Long associateId,

        @NotNull(message = "Voto é obrigatório")
        VoteValue vote

) {
}
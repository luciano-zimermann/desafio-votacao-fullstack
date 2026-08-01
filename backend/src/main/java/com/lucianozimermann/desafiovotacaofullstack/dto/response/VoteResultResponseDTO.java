package com.lucianozimermann.desafiovotacaofullstack.dto.response;

import com.lucianozimermann.desafiovotacaofullstack.enums.SessionStatus;
import lombok.Builder;

@Builder
public record VoteResultResponseDTO(

        Long agendaId,
        Long totalVotes,
        Long yesVotes,
        Long noVotes,
        String result,
        SessionStatus sessionStatus

) {
}
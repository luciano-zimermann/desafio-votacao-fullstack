package com.lucianozimermann.desafiovotacaofullstack.dto.response;

import com.lucianozimermann.desafiovotacaofullstack.enums.VoteValue;
import lombok.Builder;

@Builder
public record VoteResponseDTO(

        Long id,
        Long sessionId,
        Long associateId,
        VoteValue vote

) {
}
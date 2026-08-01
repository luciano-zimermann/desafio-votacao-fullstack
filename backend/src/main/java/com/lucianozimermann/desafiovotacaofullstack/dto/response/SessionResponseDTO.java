package com.lucianozimermann.desafiovotacaofullstack.dto.response;

import com.lucianozimermann.desafiovotacaofullstack.enums.SessionStatus;
import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record SessionResponseDTO(

        Long id,
        Long agendaId,
        Integer duration,
        LocalDateTime startDate,
        LocalDateTime endDate,
        SessionStatus status

) {
}
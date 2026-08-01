package com.lucianozimermann.desafiovotacaofullstack.dto.response;

import lombok.Builder;

@Builder
public record AssociateResponseDTO(

        Long id,
        String name,
        String cpf

) {
}
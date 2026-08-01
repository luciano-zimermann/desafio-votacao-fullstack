package com.lucianozimermann.desafiovotacaofullstack.dto.request;

import com.lucianozimermann.desafiovotacaofullstack.validation.ValidCpf;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;

@Builder
public record AssociateRequestDTO(

        @NotBlank(message = "Nome do associado é obrigatório")
        String name,

        @NotBlank(message = "CPF é obrigatório")
        @ValidCpf
        String cpf

) {
}
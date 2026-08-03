package com.lucianozimermann.desafiovotacaofullstack.controller.api.v1.swagger;

import com.lucianozimermann.desafiovotacaofullstack.dto.request.AgendaRequestDTO;
import com.lucianozimermann.desafiovotacaofullstack.dto.response.AgendaResponseDTO;
import com.lucianozimermann.desafiovotacaofullstack.dto.response.ErrorResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;

import java.util.List;

@Tag(name = "Pautas", description = "Operações de gerenciamento de pautas")
public interface AgendaSwagger {

    @Operation(summary = "Cria uma nova pauta", description = "Registra uma nova pauta no sistema.")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Pauta criada com sucesso",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = AgendaResponseDTO.class))),
            @ApiResponse(responseCode = "400", description = "Dados inválidos da pauta",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "500", description = "Erro interno no servidor",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class)))
    })
    ResponseEntity<AgendaResponseDTO> register(
            @RequestBody(description = "Dados da pauta", required = true,
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = AgendaRequestDTO.class)))
            AgendaRequestDTO dto
    );

    @Operation(summary = "Busca pauta por id", description = "Retorna os detalhes de uma pauta pelo seu id.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Pauta encontrada",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = AgendaResponseDTO.class))),
            @ApiResponse(responseCode = "404", description = "Pauta não encontrada",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "500", description = "Erro interno no servidor",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class)))
    })
    ResponseEntity<AgendaResponseDTO> findById(
            @Parameter(description = "Id da pauta", required = true) Long id
    );

    @Operation(summary = "Lista todas as pautas", description = "Retorna todas as pautas cadastradas no sistema.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Listagem retornada com sucesso",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = AgendaResponseDTO.class))),
            @ApiResponse(responseCode = "500", description = "Erro interno no servidor",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class)))
    })
    ResponseEntity<List<AgendaResponseDTO>> findAll();
}
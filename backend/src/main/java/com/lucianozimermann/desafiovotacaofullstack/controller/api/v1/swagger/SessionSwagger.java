package com.lucianozimermann.desafiovotacaofullstack.controller.api.v1.swagger;

import com.lucianozimermann.desafiovotacaofullstack.dto.request.SessionRequestDTO;
import com.lucianozimermann.desafiovotacaofullstack.dto.response.ErrorResponse;
import com.lucianozimermann.desafiovotacaofullstack.dto.response.SessionResponseDTO;
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

@Tag(name = "Sessões", description = "Operações de gerenciamento de sessões de votação")
public interface SessionSwagger {

    @Operation(summary = "Abre uma sessão de votação", description = "Abre uma sessão de votação para uma pauta, com duração em minutos informada ou 1 minuto por padrão.")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Sessão aberta com sucesso",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = SessionResponseDTO.class))),
            @ApiResponse(responseCode = "400", description = "Dados inválidos da sessão",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "404", description = "Pauta não encontrada",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "409", description = "Já existe uma sessão aberta para a pauta",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "500", description = "Erro interno no servidor",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class)))
    })
    ResponseEntity<SessionResponseDTO> open(
            @RequestBody(description = "Dados da sessão", required = true,
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = SessionRequestDTO.class)))
            SessionRequestDTO dto
    );

    @Operation(summary = "Busca sessão por id", description = "Retorna os detalhes de uma sessão pelo seu id.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Sessão encontrada",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = SessionResponseDTO.class))),
            @ApiResponse(responseCode = "404", description = "Sessão não encontrada",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "500", description = "Erro interno no servidor",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class)))
    })
    ResponseEntity<SessionResponseDTO> findById(
            @Parameter(description = "Id da sessão", required = true) Long id
    );

    @Operation(summary = "Lista todas as sessões", description = "Retorna todas as sessões de votação cadastradas no sistema.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Listagem retornada com sucesso",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = SessionResponseDTO.class))),
            @ApiResponse(responseCode = "500", description = "Erro interno no servidor",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class)))
    })
    ResponseEntity<List<SessionResponseDTO>> findAll();
}
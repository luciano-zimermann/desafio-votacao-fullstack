package com.lucianozimermann.desafiovotacaofullstack.controller.api.v1.swagger;

import com.lucianozimermann.desafiovotacaofullstack.dto.request.VoteRequestDTO;
import com.lucianozimermann.desafiovotacaofullstack.dto.response.ErrorResponse;
import com.lucianozimermann.desafiovotacaofullstack.dto.response.VoteResponseDTO;
import com.lucianozimermann.desafiovotacaofullstack.dto.response.VoteResultResponseDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;

@Tag(name = "Votos", description = "Operações de registro e apuração de votos")
public interface VoteSwagger {

    @Operation(summary = "Registra um voto", description = "Registra o voto de um associado numa sessão aberta. Cada associado só pode votar uma vez por pauta.")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Voto registrado com sucesso",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = VoteResponseDTO.class))),
            @ApiResponse(responseCode = "400", description = "Dados inválidos do voto",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "404", description = "Sessão ou associado não encontrado",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "409", description = "Sessão encerrada ou associado já votou na pauta",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "500", description = "Erro interno no servidor",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class)))
    })
    ResponseEntity<VoteResponseDTO> register(
            @RequestBody(description = "Dados do voto", required = true,
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = VoteRequestDTO.class)))
            VoteRequestDTO dto
    );

    @Operation(summary = "Consulta o resultado da votação", description = "Retorna a apuração dos votos de uma pauta e o status atual da sessão.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Resultado retornado com sucesso",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = VoteResultResponseDTO.class))),
            @ApiResponse(responseCode = "404", description = "Pauta não encontrada",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "500", description = "Erro interno no servidor",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class)))
    })
    ResponseEntity<VoteResultResponseDTO> getResult(
            @Parameter(description = "Id da pauta", required = true) Long agendaId
    );
}
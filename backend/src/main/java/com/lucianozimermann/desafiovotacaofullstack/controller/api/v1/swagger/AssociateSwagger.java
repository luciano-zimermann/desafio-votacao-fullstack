package com.lucianozimermann.desafiovotacaofullstack.controller.api.v1.swagger;

import com.lucianozimermann.desafiovotacaofullstack.dto.request.AssociateRequestDTO;
import com.lucianozimermann.desafiovotacaofullstack.dto.response.AssociateResponseDTO;
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

@Tag(name = "Associados", description = "Operações de gerenciamento de associados")
public interface AssociateSwagger {

    @Operation(summary = "Cadastra um novo associado", description = "Registra um associado no sistema, validando o CPF informado.")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Associado cadastrado com sucesso",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = AssociateResponseDTO.class))),
            @ApiResponse(responseCode = "400", description = "Dados inválidos do associado",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "409", description = "Já existe um associado cadastrado com esse CPF",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "500", description = "Erro interno no servidor",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class)))
    })
    ResponseEntity<AssociateResponseDTO> register(
            @RequestBody(description = "Dados do associado", required = true,
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = AssociateRequestDTO.class)))
            AssociateRequestDTO dto
    );

    @Operation(summary = "Busca associado por id", description = "Retorna os detalhes de um associado pelo seu id.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Associado encontrado",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = AssociateResponseDTO.class))),
            @ApiResponse(responseCode = "404", description = "Associado não encontrado",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "500", description = "Erro interno no servidor",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class)))
    })
    ResponseEntity<AssociateResponseDTO> findById(
            @Parameter(description = "Id do associado", required = true) Long id
    );

    @Operation(summary = "Lista todos os associados", description = "Retorna todos os associados cadastrados no sistema.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Listagem retornada com sucesso",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = AssociateResponseDTO.class))),
            @ApiResponse(responseCode = "500", description = "Erro interno no servidor",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class)))
    })
    ResponseEntity<List<AssociateResponseDTO>> findAll();
}
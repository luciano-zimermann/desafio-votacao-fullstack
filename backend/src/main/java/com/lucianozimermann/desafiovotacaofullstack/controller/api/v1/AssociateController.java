package com.lucianozimermann.desafiovotacaofullstack.controller.api.v1;

import com.lucianozimermann.desafiovotacaofullstack.dto.request.AssociateRequestDTO;
import com.lucianozimermann.desafiovotacaofullstack.dto.response.AssociateResponseDTO;
import com.lucianozimermann.desafiovotacaofullstack.service.AssociateService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping(value = ApiPaths.ASSOCIATES)
@RequiredArgsConstructor
public class AssociateController {

    private final AssociateService service;

    @PostMapping
    public ResponseEntity<AssociateResponseDTO> register(@Valid @RequestBody AssociateRequestDTO dto) {
        log.info("POST /associates - Cadastrando associado: {}", dto.name());

        return ResponseEntity.status(HttpStatus.CREATED).body(service.register(dto));
    }
}
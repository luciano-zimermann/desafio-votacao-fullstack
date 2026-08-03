package com.lucianozimermann.desafiovotacaofullstack.controller.api.v1;

import com.lucianozimermann.desafiovotacaofullstack.dto.request.AssociateRequestDTO;
import com.lucianozimermann.desafiovotacaofullstack.dto.response.AssociateResponseDTO;
import com.lucianozimermann.desafiovotacaofullstack.service.AssociateService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping(value = ApiPaths.ASSOCIATES)
@RequiredArgsConstructor
public class AssociateController {

    private final AssociateService service;

    @GetMapping(value = "{id}")
    public ResponseEntity<AssociateResponseDTO> findById( @PathVariable Long id) {
        log.info("GET /associates/{} - Buscando associado", id);

        return ResponseEntity.ok(service.findById(id));
    }

    @GetMapping
    public ResponseEntity<List<AssociateResponseDTO>> findAll() {
        log.info("GET /associates - Listando todas os associados");

        return ResponseEntity.ok(service.findAll());
    }

    @PostMapping
    public ResponseEntity<AssociateResponseDTO> register(@Valid @RequestBody AssociateRequestDTO dto) {
        log.info("POST /associates - Cadastrando associado: {}", dto.name());

        return ResponseEntity.status(HttpStatus.CREATED).body(service.register(dto));
    }
}
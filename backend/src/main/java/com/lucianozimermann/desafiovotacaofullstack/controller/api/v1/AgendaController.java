package com.lucianozimermann.desafiovotacaofullstack.controller.api.v1;

import com.lucianozimermann.desafiovotacaofullstack.dto.request.AgendaRequestDTO;
import com.lucianozimermann.desafiovotacaofullstack.dto.response.AgendaResponseDTO;
import com.lucianozimermann.desafiovotacaofullstack.service.AgendaService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping(value = ApiPaths.AGENDAS)
@RequiredArgsConstructor
public class AgendaController {

    private final AgendaService service;

    @GetMapping(value = "{id}")
    public ResponseEntity<AgendaResponseDTO> findById(@PathVariable Long id) {
        log.info("GET /agendas/{} - Buscando pauta", id);

        return ResponseEntity.ok(service.findById(id));
    }

    @GetMapping
    public ResponseEntity<List<AgendaResponseDTO>> findAll() {
        log.info("GET /agendas - Listando todas as pautas");

        return ResponseEntity.ok(service.findAll());
    }

    @PostMapping
    public ResponseEntity<AgendaResponseDTO> register(@Valid @RequestBody AgendaRequestDTO dto) {
        log.info("POST /agendas - Criando pauta: {}", dto.name());

        return ResponseEntity.status(HttpStatus.CREATED).body(service.register(dto));
    }
}
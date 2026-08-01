package com.lucianozimermann.desafiovotacaofullstack.controller.v1;

import com.lucianozimermann.desafiovotacaofullstack.dto.request.AgendaRequestDTO;
import com.lucianozimermann.desafiovotacaofullstack.dto.response.AgendaResponseDTO;
import com.lucianozimermann.desafiovotacaofullstack.service.AgendaService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/api/v1/agendas")
@RequiredArgsConstructor
public class AgendaController {

    private final AgendaService service;

    @GetMapping(value = "{id}")
    public ResponseEntity<AgendaResponseDTO> findById(@PathVariable Long id) {
        return ResponseEntity.ok(service.findById(id));
    }

    @PostMapping
    public ResponseEntity<AgendaResponseDTO> register(@Valid @RequestBody AgendaRequestDTO dto) {

        return ResponseEntity.status(HttpStatus.CREATED)
                             .body(service.register(dto));
    }
}
package com.lucianozimermann.desafiovotacaofullstack.controller.api.v1;

import com.lucianozimermann.desafiovotacaofullstack.controller.api.v1.swagger.SessionSwagger;
import com.lucianozimermann.desafiovotacaofullstack.dto.request.SessionRequestDTO;
import com.lucianozimermann.desafiovotacaofullstack.dto.response.SessionResponseDTO;
import com.lucianozimermann.desafiovotacaofullstack.service.SessionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping(value = ApiPaths.SESSIONS)
@RequiredArgsConstructor
public class SessionController implements SessionSwagger {

    private final SessionService service;

    @Override
    @GetMapping(value = "{id}")
    public ResponseEntity<SessionResponseDTO> findById( @PathVariable Long id) {
        log.info("GET /sessions/{} - Buscando sessão", id);

        return ResponseEntity.ok(service.findById(id));
    }

    @Override
    @GetMapping
    public ResponseEntity<List<SessionResponseDTO>> findAll() {
        log.info("GET /sessions - Listando todas as sessões");

        return ResponseEntity.ok(service.findAll());
    }

    @Override
    @PostMapping
    public ResponseEntity<SessionResponseDTO> open(@Valid @RequestBody SessionRequestDTO dto) {
        log.info("POST /sessions - Abrindo sessão para agendaId: {}", dto.agendaId());

        return ResponseEntity.status(HttpStatus.CREATED).body(service.open(dto));
    }
}
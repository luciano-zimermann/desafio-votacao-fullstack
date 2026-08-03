package com.lucianozimermann.desafiovotacaofullstack.controller.api.v1;

import com.lucianozimermann.desafiovotacaofullstack.controller.api.v1.swagger.VoteSwagger;
import com.lucianozimermann.desafiovotacaofullstack.dto.request.VoteRequestDTO;
import com.lucianozimermann.desafiovotacaofullstack.dto.response.VoteResponseDTO;
import com.lucianozimermann.desafiovotacaofullstack.dto.response.VoteResultResponseDTO;
import com.lucianozimermann.desafiovotacaofullstack.service.VoteService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping(value = ApiPaths.VOTES)
@RequiredArgsConstructor
public class VoteController implements VoteSwagger {

    private final VoteService service;

    @Override
    @PostMapping
    public ResponseEntity<VoteResponseDTO> register(@Valid @RequestBody VoteRequestDTO dto) {
        log.info("POST /votes - Registrando voto: sessionId={}, associateId={}", dto.sessionId(), dto.associateId());

        return ResponseEntity.status(HttpStatus.CREATED).body(service.register(dto));
    }

    @Override
    @GetMapping("/results/{agendaId}")
    public ResponseEntity<VoteResultResponseDTO> getResult(@PathVariable Long agendaId) {
        log.info("GET /votes/results/{} - Consultando resultado da pauta", agendaId);

        return ResponseEntity.ok(service.getResult(agendaId));
    }
}
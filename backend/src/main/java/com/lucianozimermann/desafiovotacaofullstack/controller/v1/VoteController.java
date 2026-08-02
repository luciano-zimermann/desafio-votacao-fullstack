package com.lucianozimermann.desafiovotacaofullstack.controller.v1;

import com.lucianozimermann.desafiovotacaofullstack.dto.request.VoteRequestDTO;
import com.lucianozimermann.desafiovotacaofullstack.dto.response.VoteResponseDTO;
import com.lucianozimermann.desafiovotacaofullstack.dto.response.VoteResultResponseDTO;
import com.lucianozimermann.desafiovotacaofullstack.service.VoteService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = ApiPaths.VOTES)
@RequiredArgsConstructor
public class VoteController {

    private final VoteService service;

    @PostMapping
    public ResponseEntity<VoteResponseDTO> register(@Valid @RequestBody VoteRequestDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED)
                             .body(service.register(dto));
    }

    @GetMapping("/results/{agendaId}")
    public ResponseEntity<VoteResultResponseDTO> getResult(@PathVariable Long agendaId) {
        return ResponseEntity.ok(service.getResult(agendaId));
    }
}
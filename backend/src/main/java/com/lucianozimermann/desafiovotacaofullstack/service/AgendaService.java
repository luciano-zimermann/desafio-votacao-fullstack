package com.lucianozimermann.desafiovotacaofullstack.service;

import com.lucianozimermann.desafiovotacaofullstack.dto.request.AgendaRequestDTO;
import com.lucianozimermann.desafiovotacaofullstack.dto.response.AgendaResponseDTO;
import com.lucianozimermann.desafiovotacaofullstack.entity.Agenda;
import com.lucianozimermann.desafiovotacaofullstack.exception.AgendaNotFoundException;
import com.lucianozimermann.desafiovotacaofullstack.repository.AgendaRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class AgendaService {

    private final AgendaRepository repository;

    public AgendaResponseDTO findById(Long id) {
        Agenda agenda = repository.findById(id)
                                  .orElseThrow(() -> {
                                      log.warn("Tenativa de buscar uma pauta inexistente. id={}", id);
                                      return new AgendaNotFoundException();
                                  });

        return buildAgendaResponseDTO(agenda);
    }

    public List<AgendaResponseDTO> findAll() {
        List<AgendaResponseDTO> agendas = repository.findAll()
                                                    .stream()
                                                    .map(this::buildAgendaResponseDTO)
                                                    .toList();

        log.info("Listagem de pautas retornadas. total={}", agendas.size());

        return agendas;
    }

    public AgendaResponseDTO register(AgendaRequestDTO dto) {
        Agenda agenda = Agenda.builder()
                              .name(dto.name())
                              .description(dto.description())
                              .build();

        agenda = repository.save(agenda);

        log.info("Pauta registrada com sucesso. id={}", agenda.getId());

        return buildAgendaResponseDTO(agenda);
    }

    private AgendaResponseDTO buildAgendaResponseDTO(Agenda agenda) {
        return AgendaResponseDTO.builder()
                                .id(agenda.getId())
                                .name(agenda.getName())
                                .description(agenda.getDescription())
                                .build();
    }
}
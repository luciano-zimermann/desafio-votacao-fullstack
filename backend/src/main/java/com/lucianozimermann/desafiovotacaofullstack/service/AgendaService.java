package com.lucianozimermann.desafiovotacaofullstack.service;

import com.lucianozimermann.desafiovotacaofullstack.dto.request.AgendaRequestDTO;
import com.lucianozimermann.desafiovotacaofullstack.dto.response.AgendaResponseDTO;
import com.lucianozimermann.desafiovotacaofullstack.entity.Agenda;
import com.lucianozimermann.desafiovotacaofullstack.exception.AgendaNotFoundException;
import com.lucianozimermann.desafiovotacaofullstack.repository.AgendaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AgendaService {

    private final AgendaRepository repository;

    public AgendaResponseDTO findById(Long id) {
        Agenda agenda = repository.findById(id)
                                  .orElseThrow(AgendaNotFoundException::new);

        return AgendaResponseDTO.builder()
                                .id(agenda.getId())
                                .name(agenda.getName())
                                .description(agenda.getDescription())
                                .build();
    }

    public AgendaResponseDTO register(AgendaRequestDTO dto) {
        Agenda agenda = Agenda.builder()
                              .name(dto.name())
                              .description(dto.description())
                              .build();

        agenda = repository.save(agenda);

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
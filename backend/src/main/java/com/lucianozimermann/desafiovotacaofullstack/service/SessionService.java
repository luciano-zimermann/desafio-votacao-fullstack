package com.lucianozimermann.desafiovotacaofullstack.service;

import com.lucianozimermann.desafiovotacaofullstack.dto.request.SessionRequestDTO;
import com.lucianozimermann.desafiovotacaofullstack.dto.response.SessionResponseDTO;
import com.lucianozimermann.desafiovotacaofullstack.entity.Agenda;
import com.lucianozimermann.desafiovotacaofullstack.entity.Session;
import com.lucianozimermann.desafiovotacaofullstack.exception.AgendaNotFoundException;
import com.lucianozimermann.desafiovotacaofullstack.exception.SessionAlreadyOpenException;
import com.lucianozimermann.desafiovotacaofullstack.repository.AgendaRepository;
import com.lucianozimermann.desafiovotacaofullstack.repository.SessionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class SessionService {

    private static final int DEFAULT_DURATION_MINUTES = 1;

    private final SessionRepository repository;
    private final AgendaRepository agendaRepository;

    public SessionResponseDTO open(SessionRequestDTO dto) {
        Agenda agenda = agendaRepository.findById(dto.agendaId())
                                        .orElseThrow(AgendaNotFoundException::new);

        if (repository.existsByAgendaIdAndEndDateAfter(agenda.getId(), LocalDateTime.now())) {
            throw new SessionAlreadyOpenException();
        }

        Integer duration = dto.duration() != null ? dto.duration() : DEFAULT_DURATION_MINUTES;
        LocalDateTime now = LocalDateTime.now();

        Session session = Session.builder()
                                 .agenda(agenda)
                                 .duration(duration)
                                 .startDate(now)
                                 .endDate(now.plusMinutes(duration))
                                 .build();

        session = repository.save(session);

        return buildSessionResponseDTO(session);
    }

    private SessionResponseDTO buildSessionResponseDTO(Session session) {
        return SessionResponseDTO.builder()
                                 .id(session.getId())
                                 .agendaId(session.getAgenda().getId())
                                 .duration(session.getDuration())
                                 .startDate(session.getStartDate())
                                 .endDate(session.getEndDate())
                                 .build();
    }
}

package com.lucianozimermann.desafiovotacaofullstack.service;

import com.lucianozimermann.desafiovotacaofullstack.dto.request.SessionRequestDTO;
import com.lucianozimermann.desafiovotacaofullstack.dto.response.SessionResponseDTO;
import com.lucianozimermann.desafiovotacaofullstack.entity.Agenda;
import com.lucianozimermann.desafiovotacaofullstack.entity.Session;
import com.lucianozimermann.desafiovotacaofullstack.enums.SessionStatus;
import com.lucianozimermann.desafiovotacaofullstack.exception.AgendaNotFoundException;
import com.lucianozimermann.desafiovotacaofullstack.exception.SessionAlreadyOpenException;
import com.lucianozimermann.desafiovotacaofullstack.exception.SessionNotFoundException;
import com.lucianozimermann.desafiovotacaofullstack.repository.AgendaRepository;
import com.lucianozimermann.desafiovotacaofullstack.repository.SessionRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class SessionService {

    private static final int DEFAULT_DURATION_MINUTES = 1;

    private final SessionRepository repository;
    private final AgendaRepository agendaRepository;

    public SessionResponseDTO findById( Long id) {
        Session session = repository.findById( id)
                                    .orElseThrow(() -> {
                                        log.warn("Tentativa de buscar uma sessão inexistente. id={}", id);
                                        return new SessionNotFoundException();
                                    });

        return buildSessionResponseDTO(session);
    }

    public List<SessionResponseDTO> findAll() {
        List<SessionResponseDTO> sessions = repository.findAll()
                                                      .stream()
                                                      .map(this::buildSessionResponseDTO)
                                                      .toList();

        log.info("Listagem de sessões retornadas. total={}", sessions.size());

        return sessions;
    }

    public SessionResponseDTO open(SessionRequestDTO dto) {
        Agenda agenda = agendaRepository.findById(dto.agendaId())
                                        .orElseThrow(() -> {
                                            log.warn("Tentativa de abrir sessão para uma pauta inexistente. agendaId={}", dto.agendaId());
                                            return new AgendaNotFoundException();
                                        });

        if (repository.existsByAgendaIdAndEndDateAfter(agenda.getId(), LocalDateTime.now())) {
            log.warn("Tentativa de abrir uma sessão já existente para a pauta. agendaId={}", agenda.getId());
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

        log.info("Sessão aberta com sucesso. sessionId={}, agendaId={}, duration={}min", session.getId(), agenda.getId(), duration);

        return buildSessionResponseDTO(session);
    }

    private SessionResponseDTO buildSessionResponseDTO(Session session) {
        SessionStatus status = session.isOpen() ? SessionStatus.OPEN : SessionStatus.CLOSED;

        return SessionResponseDTO.builder()
                                 .id(session.getId())
                                 .agendaId(session.getAgenda().getId())
                                 .duration(session.getDuration())
                                 .startDate(session.getStartDate())
                                 .endDate(session.getEndDate())
                                 .status( status )
                                 .build();
    }
}

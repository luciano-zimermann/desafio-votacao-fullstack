package com.lucianozimermann.desafiovotacaofullstack.service;

import com.lucianozimermann.desafiovotacaofullstack.dto.request.VoteRequestDTO;
import com.lucianozimermann.desafiovotacaofullstack.dto.response.VoteResponseDTO;
import com.lucianozimermann.desafiovotacaofullstack.dto.response.VoteResultResponseDTO;
import com.lucianozimermann.desafiovotacaofullstack.entity.Associate;
import com.lucianozimermann.desafiovotacaofullstack.entity.Session;
import com.lucianozimermann.desafiovotacaofullstack.entity.Vote;
import com.lucianozimermann.desafiovotacaofullstack.enums.SessionStatus;
import com.lucianozimermann.desafiovotacaofullstack.enums.VoteValue;
import com.lucianozimermann.desafiovotacaofullstack.exception.*;
import com.lucianozimermann.desafiovotacaofullstack.repository.AssociateRepository;
import com.lucianozimermann.desafiovotacaofullstack.repository.SessionRepository;
import com.lucianozimermann.desafiovotacaofullstack.repository.VoteRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class VoteService {

    private final VoteRepository repository;
    private final SessionRepository sessionRepository;
    private final AssociateRepository associateRepository;

    public VoteResponseDTO register(VoteRequestDTO dto) {
        Session session = sessionRepository.findById(dto.sessionId())
                                           .orElseThrow(() -> {
                                               log.warn("Tentativa de votar em uma sessão inexistente. sessionId={}", dto.sessionId());
                                               return new SessionNotFoundException();
                                           });

        Associate associate = associateRepository.findById(dto.associateId())
                                                 .orElseThrow(() -> {
                                                     log.warn("Tentativa de voto de associado inexistente. associateId={}", dto.associateId());
                                                     return new AssociateNotFoundException();
                                                 });

        if (!session.isOpen()) {
            log.warn("Tentativa de voto em uma sessão encerrada. sessionId={}", session.getId());
            throw new SessionClosedException();
        }

        Long agendaId = session.getAgenda().getId();

        if (repository.existsByAssociateIdAndSessionAgendaId(associate.getId(), agendaId)) {
            log.warn("Tentativa de voto duplicado. associateId={}, agendaId={}", associate.getId(), agendaId);
            throw new VoteAlreadyExistsException();
        }

        Vote vote = Vote.builder()
                        .session(session)
                        .associate(associate)
                        .vote(dto.vote())
                        .build();

        vote = repository.save(vote);

        log.info("Voto registrado com sucesso. voteId={}, sessionId={}, associateId={}", vote.getId(), session.getId(), associate.getId());

        return buildVoteResponseDTO(vote);
    }

    public VoteResultResponseDTO getResult(Long agendaId) {
        long yesVotes = repository.countBySessionAgendaIdAndVote(agendaId, VoteValue.YES);
        long noVotes = repository.countBySessionAgendaIdAndVote(agendaId, VoteValue.NO);
        long totalVotes = yesVotes + noVotes;

        String result = yesVotes > noVotes ? "Aprovada" : "Rejeitada";

        SessionStatus sessionStatus = sessionRepository.findFirstByAgendaIdOrderByStartDateDesc(agendaId)
                                                       .map(session -> session.isOpen() ? SessionStatus.OPEN
                                                                                        : SessionStatus.CLOSED)
                                                       .orElse(null);

        log.info("Resultado da Pauta apurado. agendaId={}, totalVotes={}, result={}", agendaId, totalVotes, result);

        return VoteResultResponseDTO.builder()
                                    .agendaId(agendaId)
                                    .totalVotes(totalVotes)
                                    .yesVotes(yesVotes)
                                    .noVotes(noVotes)
                                    .result(result)
                                    .sessionStatus(sessionStatus)
                                    .build();
    }

    private VoteResponseDTO buildVoteResponseDTO(Vote vote) {
        return VoteResponseDTO.builder()
                              .id(vote.getId())
                              .sessionId(vote.getSession().getId())
                              .associateId(vote.getAssociate().getId())
                              .vote(vote.getVote())
                              .build();
    }
}
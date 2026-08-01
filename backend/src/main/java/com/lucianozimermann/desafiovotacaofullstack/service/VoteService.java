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
import com.lucianozimermann.desafiovotacaofullstack.utils.SessionUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class VoteService {

    private final VoteRepository repository;
    private final SessionRepository sessionRepository;
    private final AssociateRepository associateRepository;

    public VoteResponseDTO create(VoteRequestDTO dto) {
        Session session = sessionRepository.findById(dto.sessionId())
                                           .orElseThrow(SessionNotFoundException::new);

        Associate associate = associateRepository.findById(dto.associateId())
                                                 .orElseThrow(AssociateNotFoundException::new);

        if (!SessionUtils.isOpen(session)) {
            throw new SessionClosedException();
        }

        Long agendaId = session.getAgenda().getId();

        if (repository.existsByAssociateIdAndSessionAgendaId(associate.getId(), agendaId)) {
            throw new VoteAlreadyExistsException();
        }

        Vote vote = Vote.builder()
                        .session(session)
                        .associate(associate)
                        .vote(dto.vote())
                        .build();

        vote = repository.save(vote);

        return buildVoteResponseDTO(vote);
    }

    public VoteResultResponseDTO getResult(Long agendaId) {
        long yesVotes = repository.countBySessionAgendaIdAndVote(agendaId, VoteValue.YES);
        long noVotes = repository.countBySessionAgendaIdAndVote(agendaId, VoteValue.NO);
        long totalVotes = yesVotes + noVotes;

        String result = yesVotes > noVotes ? "Aprovada" : "Rejeitada";

        SessionStatus sessionStatus = sessionRepository.findFirstByAgendaIdOrderByStartDateDesc(agendaId)
                                                       .map(session -> SessionUtils.isOpen(session) ? SessionStatus.OPEN
                                                                                                    : SessionStatus.CLOSED)
                                                       .orElse(null);

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
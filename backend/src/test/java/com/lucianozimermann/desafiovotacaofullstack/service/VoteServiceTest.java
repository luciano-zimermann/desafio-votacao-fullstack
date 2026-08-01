package com.lucianozimermann.desafiovotacaofullstack.service;

import com.lucianozimermann.desafiovotacaofullstack.dto.request.VoteRequestDTO;
import com.lucianozimermann.desafiovotacaofullstack.dto.response.VoteResponseDTO;
import com.lucianozimermann.desafiovotacaofullstack.dto.response.VoteResultResponseDTO;
import com.lucianozimermann.desafiovotacaofullstack.entity.Agenda;
import com.lucianozimermann.desafiovotacaofullstack.entity.Associate;
import com.lucianozimermann.desafiovotacaofullstack.entity.Session;
import com.lucianozimermann.desafiovotacaofullstack.entity.Vote;
import com.lucianozimermann.desafiovotacaofullstack.enums.SessionStatus;
import com.lucianozimermann.desafiovotacaofullstack.enums.VoteValue;
import com.lucianozimermann.desafiovotacaofullstack.exception.AssociateNotFoundException;
import com.lucianozimermann.desafiovotacaofullstack.exception.SessionClosedException;
import com.lucianozimermann.desafiovotacaofullstack.exception.SessionNotFoundException;
import com.lucianozimermann.desafiovotacaofullstack.exception.VoteAlreadyExistsException;
import com.lucianozimermann.desafiovotacaofullstack.repository.AssociateRepository;
import com.lucianozimermann.desafiovotacaofullstack.repository.SessionRepository;
import com.lucianozimermann.desafiovotacaofullstack.repository.VoteRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
class VoteServiceTest {

    private static final Long AGENDA_ID = 1L;
    private static final Long SESSION_ID = 1L;
    private static final Long ASSOCIATE_ID = 1L;
    private static final Long VOTE_ID = 1L;

    @Mock
    private VoteRepository repository;

    @Mock
    private SessionRepository sessionRepository;

    @Mock
    private AssociateRepository associateRepository;

    @InjectMocks
    private VoteService service;

    @Test
    @DisplayName("Deve registrar o voto quando sessão estiver aberta e associado não tiver votado")
    void shouldRegisterVoteWhenSessionOpenAndAssociateHasNotVoted() {
        VoteRequestDTO request = new VoteRequestDTO(SESSION_ID, ASSOCIATE_ID, VoteValue.YES);
        Session session = buildOpenSession();
        Associate associate = buildAssociate();

        Mockito.when(sessionRepository.findById(SESSION_ID)).thenReturn(Optional.of(session));
        Mockito.when(associateRepository.findById(ASSOCIATE_ID)).thenReturn(Optional.of(associate));
        Mockito.when(repository.existsByAssociateIdAndSessionAgendaId(ASSOCIATE_ID, AGENDA_ID)).thenReturn(false);
        Mockito.when(repository.save(Mockito.any(Vote.class))).thenReturn(buildVote(session, associate));

        VoteResponseDTO response = service.register(request);

        Assertions.assertThat(response).isNotNull();
        Assertions.assertThat(response.id()).isEqualTo(VOTE_ID);
        Assertions.assertThat(response.sessionId()).isEqualTo(SESSION_ID);
        Assertions.assertThat(response.associateId()).isEqualTo(ASSOCIATE_ID);
        Assertions.assertThat(response.vote()).isEqualTo(VoteValue.YES);

        Mockito.verify(repository, Mockito.times(1)).save(Mockito.any(Vote.class));
    }

    @Test
    @DisplayName("Deve lançar exceção quando a sessão não existir")
    void shouldThrowExceptionWhenSessionDoesNotExist() {
        VoteRequestDTO request = new VoteRequestDTO(SESSION_ID, ASSOCIATE_ID, VoteValue.YES);

        Mockito.when(sessionRepository.findById(SESSION_ID)).thenReturn(Optional.empty());

        Assertions.assertThatThrownBy(() -> service.register(request)).isInstanceOf(SessionNotFoundException.class);

        Mockito.verify(repository, Mockito.never()).save(Mockito.any(Vote.class));
    }

    @Test
    @DisplayName("Deve lançar exceção quando o associado não existir")
    void shouldThrowExceptionWhenAssociateDoesNotExist() {
        VoteRequestDTO request = new VoteRequestDTO(SESSION_ID, ASSOCIATE_ID, VoteValue.YES);

        Mockito.when(sessionRepository.findById(SESSION_ID)).thenReturn(Optional.of(buildOpenSession()));
        Mockito.when(associateRepository.findById(ASSOCIATE_ID)).thenReturn(Optional.empty());

        Assertions.assertThatThrownBy(() -> service.register(request)).isInstanceOf(AssociateNotFoundException.class);

        Mockito.verify(repository, Mockito.never()).save(Mockito.any(Vote.class));
    }

    @Test
    @DisplayName("Deve lançar exceção quando a sessão já estiver encerrada")
    void shouldThrowExceptionWhenSessionIsClosed() {
        VoteRequestDTO request = new VoteRequestDTO(SESSION_ID, ASSOCIATE_ID, VoteValue.YES);

        Mockito.when(sessionRepository.findById(SESSION_ID)).thenReturn(Optional.of(buildClosedSession()));
        Mockito.when(associateRepository.findById(ASSOCIATE_ID)).thenReturn(Optional.of(buildAssociate()));

        Assertions.assertThatThrownBy(() -> service.register(request)).isInstanceOf(SessionClosedException.class);

        Mockito.verify(repository, Mockito.never()).save(Mockito.any(Vote.class));
    }

    @Test
    @DisplayName("Deve lançar exceção quando o associado já tiver votado na pauta")
    void shouldThrowExceptionWhenAssociateAlreadyVotedOnAgenda() {
        VoteRequestDTO request = new VoteRequestDTO(SESSION_ID, ASSOCIATE_ID, VoteValue.YES);

        Mockito.when(sessionRepository.findById(SESSION_ID)).thenReturn(Optional.of(buildOpenSession()));
        Mockito.when(associateRepository.findById(ASSOCIATE_ID)).thenReturn(Optional.of(buildAssociate()));
        Mockito.when(repository.existsByAssociateIdAndSessionAgendaId(ASSOCIATE_ID, AGENDA_ID)).thenReturn(true);

        Assertions.assertThatThrownBy(() -> service.register(request)).isInstanceOf(VoteAlreadyExistsException.class);

        Mockito.verify(repository, Mockito.never()).save(Mockito.any(Vote.class));
    }

    @Test
    @DisplayName("Deve retornar Aprovada quando os votos Sim forem maioria")
    void shouldReturnApprovedWhenYesVotesAreMajority() {
        Mockito.when(repository.countBySessionAgendaIdAndVote(AGENDA_ID, VoteValue.YES)).thenReturn(3L);
        Mockito.when(repository.countBySessionAgendaIdAndVote(AGENDA_ID, VoteValue.NO)).thenReturn(1L);
        Mockito.when(sessionRepository.findFirstByAgendaIdOrderByStartDateDesc(AGENDA_ID))
               .thenReturn(Optional.of(buildOpenSession()));

        VoteResultResponseDTO result = service.getResult(AGENDA_ID);

        Assertions.assertThat(result.totalVotes()).isEqualTo(4L);
        Assertions.assertThat(result.yesVotes()).isEqualTo(3L);
        Assertions.assertThat(result.noVotes()).isEqualTo(1L);
        Assertions.assertThat(result.result()).isEqualTo("Aprovada");
        Assertions.assertThat(result.sessionStatus()).isEqualTo(SessionStatus.OPEN);
    }

    @Test
    @DisplayName("Deve retornar Rejeitada quando os votos Não forem maioria")
    void shouldReturnRejectedWhenNoVotesAreMajority() {
        Mockito.when(repository.countBySessionAgendaIdAndVote(AGENDA_ID, VoteValue.YES)).thenReturn(1L);
        Mockito.when(repository.countBySessionAgendaIdAndVote(AGENDA_ID, VoteValue.NO)).thenReturn(3L);
        Mockito.when(sessionRepository.findFirstByAgendaIdOrderByStartDateDesc(AGENDA_ID))
               .thenReturn(Optional.of(buildClosedSession()));

        VoteResultResponseDTO result = service.getResult(AGENDA_ID);

        Assertions.assertThat(result.result()).isEqualTo("Rejeitada");
        Assertions.assertThat(result.sessionStatus()).isEqualTo(SessionStatus.CLOSED);
    }

    @Test
    @DisplayName("Deve retornar Rejeitada quando houver empate entre os votos")
    void shouldReturnRejectedWhenVotesAreTied() {
        Mockito.when(repository.countBySessionAgendaIdAndVote(AGENDA_ID, VoteValue.YES)).thenReturn(2L);
        Mockito.when(repository.countBySessionAgendaIdAndVote(AGENDA_ID, VoteValue.NO)).thenReturn(2L);
        Mockito.when(sessionRepository.findFirstByAgendaIdOrderByStartDateDesc(AGENDA_ID))
               .thenReturn(Optional.of(buildClosedSession()));

        VoteResultResponseDTO result = service.getResult(AGENDA_ID);

        Assertions.assertThat(result.result()).isEqualTo("Rejeitada");
    }

    @Test
    @DisplayName("Deve retornar sessionStatus nulo quando a pauta não tiver nenhuma sessão")
    void shouldReturnNullSessionStatusWhenAgendaHasNoSession() {
        Mockito.when(repository.countBySessionAgendaIdAndVote(AGENDA_ID, VoteValue.YES)).thenReturn(0L);
        Mockito.when(repository.countBySessionAgendaIdAndVote(AGENDA_ID, VoteValue.NO)).thenReturn(0L);
        Mockito.when(sessionRepository.findFirstByAgendaIdOrderByStartDateDesc(AGENDA_ID))
               .thenReturn(Optional.empty());

        VoteResultResponseDTO result = service.getResult(AGENDA_ID);

        Assertions.assertThat(result.sessionStatus()).isNull();
    }

    private Agenda buildAgenda() {
        return Agenda.builder()
                     .id(AGENDA_ID)
                     .name("Nova pauta")
                     .description("Descrição da pauta")
                     .build();
    }

    private Session buildOpenSession() {
        return Session.builder()
                      .id(SESSION_ID)
                      .agenda(buildAgenda())
                      .endDate(LocalDateTime.now().plusMinutes(1))
                      .build();
    }

    private Session buildClosedSession() {
        return Session.builder()
                      .id(SESSION_ID)
                      .agenda(buildAgenda())
                      .endDate(LocalDateTime.now().minusMinutes(1))
                      .build();
    }

    private Associate buildAssociate() {
        return Associate.builder()
                        .id(ASSOCIATE_ID)
                        .name("Renato Ferreira")
                        .cpf("52998224725")
                        .build();
    }

    private Vote buildVote(Session session, Associate associate) {
        return Vote.builder()
                   .id(VOTE_ID)
                   .session(session)
                   .associate(associate)
                   .vote(VoteValue.YES)
                   .build();
    }
}
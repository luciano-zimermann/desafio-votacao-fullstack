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
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
class SessionServiceTest {

    private static final Long AGENDA_ID = 1L;
    private static final Long SESSION_ID = 1L;
    private static final Integer DEFAULT_DURATION = 1;
    private static final Integer CUSTOM_DURATION = 5;

    @Mock
    private SessionRepository repository;

    @Mock
    private AgendaRepository agendaRepository;

    @InjectMocks
    private SessionService service;

    @Test
    @DisplayName("Deve abrir uma sessão com a duração padrão quando não informada")
    void shouldOpenSessionWithDefaultDurationWhenNotProvided() {
        SessionRequestDTO request = new SessionRequestDTO(AGENDA_ID, null);

        Mockito.when(agendaRepository.findById(AGENDA_ID)).thenReturn(Optional.of(buildAgenda()));
        Mockito.when(repository.existsByAgendaIdAndEndDateAfter(Mockito.eq(AGENDA_ID), Mockito.any(LocalDateTime.class)))
               .thenReturn(false);
        Mockito.when(repository.save(Mockito.any(Session.class))).thenReturn(buildSession(DEFAULT_DURATION));

        SessionResponseDTO response = service.open(request);

        Assertions.assertThat(response).isNotNull();
        Assertions.assertThat(response.id()).isEqualTo(SESSION_ID);
        Assertions.assertThat(response.agendaId()).isEqualTo(AGENDA_ID);
        Assertions.assertThat(response.duration()).isEqualTo(DEFAULT_DURATION);
        Assertions.assertThat(response.status()).isEqualTo(SessionStatus.OPEN);

        Mockito.verify(repository, Mockito.times(1)).save(Mockito.any(Session.class));
    }

    @Test
    @DisplayName("Deve abrir uma sessão com a duração customizada informada")
    void shouldOpenSessionWithCustomDurationWhenProvided() {
        SessionRequestDTO request = new SessionRequestDTO(AGENDA_ID, CUSTOM_DURATION);

        Mockito.when(agendaRepository.findById(AGENDA_ID)).thenReturn(Optional.of(buildAgenda()));
        Mockito.when(repository.existsByAgendaIdAndEndDateAfter(Mockito.eq(AGENDA_ID), Mockito.any(LocalDateTime.class)))
               .thenReturn(false);
        Mockito.when(repository.save(Mockito.any(Session.class))).thenReturn(buildSession(CUSTOM_DURATION));

        SessionResponseDTO response = service.open(request);

        Assertions.assertThat(response.duration()).isEqualTo(CUSTOM_DURATION);
        Assertions.assertThat(response.status()).isEqualTo(SessionStatus.OPEN);
    }

    @Test
    @DisplayName("Deve retornar a sessão pelo id")
    void shouldFindSessionById() {
        Mockito.when(repository.findById(SESSION_ID)).thenReturn(Optional.of(buildSession(DEFAULT_DURATION)));

        SessionResponseDTO response = service.findById(SESSION_ID);

        Assertions.assertThat(response.id()).isEqualTo(SESSION_ID);
        Assertions.assertThat(response.status()).isEqualTo(SessionStatus.OPEN);
    }

    @Test
    @DisplayName("Deve lançar exceção quando a sessão não existir ao buscar por id")
    void shouldThrowExceptionWhenSessionDoesNotExistOnFindById() {
        Mockito.when(repository.findById(SESSION_ID)).thenReturn(Optional.empty());

        Assertions.assertThatThrownBy(() -> service.findById(SESSION_ID)).isInstanceOf(SessionNotFoundException.class);
    }

    @Test
    @DisplayName("Deve retornar a lista de todas as sessões cadastradas")
    void shouldReturnAllSessions() {
        Mockito.when(repository.findAll()).thenReturn(List.of(buildSession(DEFAULT_DURATION)));

        List<SessionResponseDTO> response = service.findAll();

        Assertions.assertThat(response).hasSize(1);
        Assertions.assertThat(response.getFirst().id()).isEqualTo(SESSION_ID);
    }

    @Test
    @DisplayName("Deve lançar exceção quando a pauta não existir")
    void shouldThrowExceptionWhenAgendaDoesNotExist() {
        SessionRequestDTO request = new SessionRequestDTO(AGENDA_ID, null);

        Mockito.when(agendaRepository.findById(AGENDA_ID)).thenReturn(Optional.empty());

        Assertions.assertThatThrownBy(() -> service.open(request)).isInstanceOf(AgendaNotFoundException.class);

        Mockito.verify(repository, Mockito.never()).save(Mockito.any(Session.class));
    }

    @Test
    @DisplayName("Deve lançar exceção quando já existir sessão aberta para a pauta")
    void shouldThrowExceptionWhenSessionAlreadyOpenForAgenda() {
        SessionRequestDTO request = new SessionRequestDTO(AGENDA_ID, null);

        Mockito.when(agendaRepository.findById(AGENDA_ID)).thenReturn(Optional.of(buildAgenda()));
        Mockito.when(repository.existsByAgendaIdAndEndDateAfter(Mockito.eq(AGENDA_ID), Mockito.any(LocalDateTime.class)))
               .thenReturn(true);

        Assertions.assertThatThrownBy(() -> service.open(request)).isInstanceOf(SessionAlreadyOpenException.class);

        Mockito.verify(repository, Mockito.never()).save(Mockito.any(Session.class));
    }

    private Agenda buildAgenda() {
        return Agenda.builder()
                     .id(AGENDA_ID)
                     .name("Nova pauta")
                     .description("Descrição da pauta")
                     .build();
    }

    private Session buildSession(Integer duration) {
        LocalDateTime now = LocalDateTime.now();

        return Session.builder()
                      .id(SESSION_ID)
                      .agenda(buildAgenda())
                      .duration(duration)
                      .startDate(now)
                      .endDate(now.plusMinutes(duration))
                      .build();
    }
}
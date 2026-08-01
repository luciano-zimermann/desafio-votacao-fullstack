package com.lucianozimermann.desafiovotacaofullstack.repository;

import com.lucianozimermann.desafiovotacaofullstack.entity.Agenda;
import com.lucianozimermann.desafiovotacaofullstack.entity.Session;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.time.LocalDateTime;

@DataJpaTest
class SessionRepositoryTest {

    private static final Integer DURATION = 1;

    @Autowired
    private AgendaRepository agendaRepository;

    @Autowired
    private SessionRepository repository;

    @Test
    @DisplayName("Deve retornar true quando existir sessão ainda aberta para a pauta")
    void shouldReturnTrueWhenSessionIsStillOpenForAgenda() {
        Agenda agenda = agendaRepository.save(buildAgenda());
        LocalDateTime now = LocalDateTime.now();

        repository.save(buildSession(agenda, now, now.plusMinutes(DURATION)));

        boolean exists = repository.existsByAgendaIdAndEndDateAfter(agenda.getId(), LocalDateTime.now());

        Assertions.assertThat(exists).isTrue();
    }

    @Test
    @DisplayName("Deve retornar false quando a sessão da pauta já estiver expirada")
    void shouldReturnFalseWhenSessionIsAlreadyExpired() {
        Agenda agenda = agendaRepository.save(buildAgenda());
        LocalDateTime now = LocalDateTime.now();

        repository.save(buildSession(agenda, now.minusMinutes(10), now.minusMinutes(5)));

        boolean exists = repository.existsByAgendaIdAndEndDateAfter(agenda.getId(), LocalDateTime.now());

        Assertions.assertThat(exists).isFalse();
    }

    @Test
    @DisplayName("Deve retornar false quando a pauta não tiver nenhuma sessão")
    void shouldReturnFalseWhenAgendaHasNoSession() {
        Agenda agenda = agendaRepository.save(buildAgenda());

        boolean exists = repository.existsByAgendaIdAndEndDateAfter(agenda.getId(), LocalDateTime.now());

        Assertions.assertThat(exists).isFalse();
    }

    private Agenda buildAgenda() {
        return Agenda.builder()
                     .name("Nova pauta")
                     .description("Descrição da pauta")
                     .build();
    }

    private Session buildSession(Agenda agenda, LocalDateTime startDate, LocalDateTime endDate) {
        return Session.builder()
                      .agenda(agenda)
                      .duration(DURATION)
                      .startDate(startDate)
                      .endDate(endDate)
                      .build();
    }
}
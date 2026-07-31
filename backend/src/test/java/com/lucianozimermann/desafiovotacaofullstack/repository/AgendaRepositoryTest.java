package com.lucianozimermann.desafiovotacaofullstack.repository;

import com.lucianozimermann.desafiovotacaofullstack.entity.Agenda;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Optional;

@DataJpaTest
class AgendaRepositoryTest {

    private static final Long NON_EXISTENT_AGENDA_ID = -1L;
    private static final String AGENDA_NAME = "Nova pauta";
    private static final String AGENDA_DESCRIPTION = "Descrição da pauta";

    @Autowired
    private AgendaRepository repository;

    @Test
    @DisplayName("Deve salvar uma pauta e gerar o id automaticamente")
    void shouldSaveAgendaAndGenerateId() {
        Agenda agenda = buildAgenda();

        Agenda saved = repository.save(agenda);

        Assertions.assertThat( saved.getId()).isNotNull();
        Assertions.assertThat(saved.getName()).isEqualTo(AGENDA_NAME);
        Assertions.assertThat(saved.getDescription()).isEqualTo(AGENDA_DESCRIPTION);
    }

    @Test
    @DisplayName("Deve buscar uma pauta persistida pelo id")
    void shouldFindAgendaById() {
        Agenda saved = repository.save(buildAgenda());

        Optional<Agenda> found = repository.findById(saved.getId());

        Assertions.assertThat(found).isPresent();
        Assertions.assertThat(found.get().getName()).isEqualTo(AGENDA_NAME);
    }

    @Test
    @DisplayName("Não deve encontrar uma pauta com id inexistente")
    void shouldNotFindAgendaWithNonExistentId() {
        Optional<Agenda> found = repository.findById(NON_EXISTENT_AGENDA_ID);

        Assertions.assertThat(found).isEmpty();
    }

    private Agenda buildAgenda() {
        return Agenda.builder()
                     .name(AGENDA_NAME)
                     .description(AGENDA_DESCRIPTION)
                     .build();
    }
}
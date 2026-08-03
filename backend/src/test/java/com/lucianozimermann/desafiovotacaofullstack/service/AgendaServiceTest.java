package com.lucianozimermann.desafiovotacaofullstack.service;

import com.lucianozimermann.desafiovotacaofullstack.dto.request.AgendaRequestDTO;
import com.lucianozimermann.desafiovotacaofullstack.dto.response.AgendaResponseDTO;
import com.lucianozimermann.desafiovotacaofullstack.entity.Agenda;
import com.lucianozimermann.desafiovotacaofullstack.exception.AgendaNotFoundException;
import com.lucianozimermann.desafiovotacaofullstack.repository.AgendaRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
class AgendaServiceTest {

    private static final Long AGENDA_ID = 1L;
    private static final String AGENDA_NAME = "Nova pauta";
    private static final String AGENDA_DESCRIPTION = "Descrição da pauta";

    @Mock
    private AgendaRepository repository;

    @InjectMocks
    private AgendaService service;

    @Test
    @DisplayName("Deve criar uma pauta corretamente")
    void shouldCreateAgenda() {
        AgendaRequestDTO request = new AgendaRequestDTO(AGENDA_NAME, AGENDA_DESCRIPTION);

        Agenda agenda = buildAgenda();

        Mockito.when(repository.save(Mockito.any(Agenda.class))).thenReturn(agenda);

        AgendaResponseDTO response = service.register(request);

        Assertions.assertThat( response).isNotNull();
        Assertions.assertThat(response.id()).isEqualTo(AGENDA_ID);
        Assertions.assertThat(response.name()).isEqualTo(AGENDA_NAME);
        Assertions.assertThat(response.description()).isEqualTo(AGENDA_DESCRIPTION);

        Mockito.verify(repository, Mockito.times(1)).save(Mockito.any(Agenda.class));
    }

    @Test
    @DisplayName("Deve buscar uma pauta pelo ID")
    void shouldFindAgendaById() {
        Agenda agenda = buildAgenda();

        Mockito.when(repository.findById(AGENDA_ID)).thenReturn(Optional.of(agenda));

        AgendaResponseDTO response = service.findById(AGENDA_ID);

        Assertions.assertThat(response).isNotNull();
        Assertions.assertThat(response.id()).isEqualTo(AGENDA_ID);
        Assertions.assertThat(response.name()).isEqualTo(AGENDA_NAME);
        Assertions.assertThat(response.description()).isEqualTo(AGENDA_DESCRIPTION);

        Mockito.verify(repository, Mockito.times(1)).findById(AGENDA_ID);
    }

    @Test
    @DisplayName("Deve retornar a lista de todas as pautas cadastradas")
    void shouldReturnAllAgendas() {
        Mockito.when(repository.findAll()).thenReturn( List.of( buildAgenda()));

        List<AgendaResponseDTO> response = service.findAll();

        Assertions.assertThat(response).hasSize(1);
        Assertions.assertThat(response.getFirst().id()).isEqualTo(AGENDA_ID);
        Assertions.assertThat(response.getFirst().name()).isEqualTo(AGENDA_NAME);

        Mockito.verify(repository, Mockito.times(1)).findAll();
    }

    @Test
    @DisplayName("Deve lançar exceção quando a pauta não existir")
    void shouldThrowExceptionWhenAgendaDoesNotExist() {
        Mockito.when(repository.findById(AGENDA_ID)).thenReturn(Optional.empty());

        Assertions.assertThatThrownBy(() -> service.findById(AGENDA_ID)).isInstanceOf(AgendaNotFoundException.class);

        Mockito.verify(repository, Mockito.times(1)).findById(AGENDA_ID);
    }

    private Agenda buildAgenda() {
        return Agenda.builder()
                     .id(AGENDA_ID)
                     .name(AGENDA_NAME)
                     .description(AGENDA_DESCRIPTION)
                     .build();
    }
}
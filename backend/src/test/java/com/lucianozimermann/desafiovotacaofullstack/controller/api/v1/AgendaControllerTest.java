package com.lucianozimermann.desafiovotacaofullstack.controller.api.v1;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.lucianozimermann.desafiovotacaofullstack.dto.request.AgendaRequestDTO;
import com.lucianozimermann.desafiovotacaofullstack.dto.response.AgendaResponseDTO;
import com.lucianozimermann.desafiovotacaofullstack.exception.AgendaNotFoundException;
import com.lucianozimermann.desafiovotacaofullstack.exception.GlobalExceptionHandler;
import com.lucianozimermann.desafiovotacaofullstack.service.AgendaService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

@ExtendWith(MockitoExtension.class)
class AgendaControllerTest {

    private static final Long AGENDA_ID = 1L;
    private static final String AGENDA_NAME = "Nova pauta";
    private static final String AGENDA_DESCRIPTION = "Descrição da pauta";

    @Mock
    private AgendaService service;

    private MockMvc mockMvc;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    void setup() {
        mockMvc = MockMvcBuilders.standaloneSetup(new AgendaController(service))
                                 .setControllerAdvice(new GlobalExceptionHandler())
                                 .build();
    }

    @Test
    @DisplayName("Deve retornar 200 quando a pauta existir")
    void shouldReturn200WhenAgendaExists() throws Exception {
        Mockito.when(service.findById(AGENDA_ID)).thenReturn(buildAgendaResponse());

        mockMvc.perform(MockMvcRequestBuilders.get(ApiPaths.AGENDAS + "/{id}", AGENDA_ID))
               .andExpect(MockMvcResultMatchers.status().isOk())
               .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(AGENDA_ID))
               .andExpect(MockMvcResultMatchers.jsonPath("$.name").value(AGENDA_NAME));
    }

    @Test
    @DisplayName("Deve retornar 404 quando a pauta não existir")
    void shouldReturn404WhenAgendaDoesNotExist() throws Exception {
        Mockito.when(service.findById(AGENDA_ID)).thenThrow(new AgendaNotFoundException());

        mockMvc.perform(MockMvcRequestBuilders.get(ApiPaths.AGENDAS + "/{id}", AGENDA_ID))
               .andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    @Test
    @DisplayName("Deve retornar 201 quando os dados da pauta forem válidos")
    void shouldReturn201WhenRequestIsValid() throws Exception {
        AgendaRequestDTO request = new AgendaRequestDTO(AGENDA_NAME, AGENDA_DESCRIPTION);

        Mockito.when(service.register(Mockito.any(AgendaRequestDTO.class))).thenReturn(buildAgendaResponse());

        mockMvc.perform(MockMvcRequestBuilders.post(ApiPaths.AGENDAS)
                                              .contentType(MediaType.APPLICATION_JSON)
                                              .content(objectMapper.writeValueAsString(request)))
               .andExpect(MockMvcResultMatchers.status().isCreated())
               .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(AGENDA_ID));
    }

    @Test
    @DisplayName("Deve retornar 400 quando o nome da pauta estiver em branco")
    void shouldReturn400WhenNameIsBlank() throws Exception {
        AgendaRequestDTO request = new AgendaRequestDTO("", AGENDA_DESCRIPTION);

        mockMvc.perform(MockMvcRequestBuilders.post(ApiPaths.AGENDAS)
                                              .contentType(MediaType.APPLICATION_JSON)
                                              .content(objectMapper.writeValueAsString(request)))
               .andExpect(MockMvcResultMatchers.status().isBadRequest());

        Mockito.verify(service, Mockito.never()).register(Mockito.any());
    }

    private AgendaResponseDTO buildAgendaResponse() {
        return AgendaResponseDTO.builder()
                                .id(AGENDA_ID)
                                .name(AGENDA_NAME)
                                .description(AGENDA_DESCRIPTION)
                                .build();
    }
}
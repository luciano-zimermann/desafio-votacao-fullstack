package com.lucianozimermann.desafiovotacaofullstack.controller.v1;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.lucianozimermann.desafiovotacaofullstack.dto.request.SessionRequestDTO;
import com.lucianozimermann.desafiovotacaofullstack.dto.response.SessionResponseDTO;
import com.lucianozimermann.desafiovotacaofullstack.enums.SessionStatus;
import com.lucianozimermann.desafiovotacaofullstack.exception.AgendaNotFoundException;
import com.lucianozimermann.desafiovotacaofullstack.exception.GlobalExceptionHandler;
import com.lucianozimermann.desafiovotacaofullstack.exception.SessionAlreadyOpenException;
import com.lucianozimermann.desafiovotacaofullstack.service.SessionService;
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

import java.time.LocalDateTime;

@ExtendWith(MockitoExtension.class)
class SessionControllerTest {

    private static final Long AGENDA_ID = 1L;
    private static final Long SESSION_ID = 1L;
    private static final Integer DURATION = 1;

    @Mock
    private SessionService service;

    private MockMvc mockMvc;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    void setup() {
        mockMvc = MockMvcBuilders.standaloneSetup(new SessionController(service))
                                 .setControllerAdvice(new GlobalExceptionHandler())
                                 .build();
    }

    @Test
    @DisplayName("Deve retornar 201 quando os dados da sessão forem válidos")
    void shouldReturn201WhenRequestIsValid() throws Exception {
        SessionRequestDTO request = new SessionRequestDTO(AGENDA_ID, DURATION);

        Mockito.when(service.open(Mockito.any(SessionRequestDTO.class))).thenReturn(buildSessionResponse());

        mockMvc.perform(MockMvcRequestBuilders.post(ApiPaths.SESSIONS)
                                              .contentType(MediaType.APPLICATION_JSON)
                                              .content(objectMapper.writeValueAsString(request)))
               .andExpect(MockMvcResultMatchers.status().isCreated())
               .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(SESSION_ID))
               .andExpect(MockMvcResultMatchers.jsonPath("$.status").value("OPEN"));
    }

    @Test
    @DisplayName("Deve retornar 404 quando a pauta não existir")
    void shouldReturn404WhenAgendaDoesNotExist() throws Exception {
        SessionRequestDTO request = new SessionRequestDTO(AGENDA_ID, DURATION);

        Mockito.when(service.open(Mockito.any(SessionRequestDTO.class))).thenThrow(new AgendaNotFoundException());

        mockMvc.perform(MockMvcRequestBuilders.post(ApiPaths.SESSIONS)
                                              .contentType(MediaType.APPLICATION_JSON)
                                              .content(objectMapper.writeValueAsString(request)))
               .andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    @Test
    @DisplayName("Deve retornar 409 quando já existir sessão aberta para a pauta")
    void shouldReturn409WhenSessionAlreadyOpen() throws Exception {
        SessionRequestDTO request = new SessionRequestDTO(AGENDA_ID, DURATION);

        Mockito.when(service.open(Mockito.any(SessionRequestDTO.class))).thenThrow(new SessionAlreadyOpenException());

        mockMvc.perform(MockMvcRequestBuilders.post(ApiPaths.SESSIONS)
                                              .contentType(MediaType.APPLICATION_JSON)
                                              .content(objectMapper.writeValueAsString(request)))
               .andExpect(MockMvcResultMatchers.status().isConflict());
    }

    @Test
    @DisplayName("Deve retornar 400 quando o id da pauta não for informado")
    void shouldReturn400WhenAgendaIdIsNull() throws Exception {
        SessionRequestDTO request = new SessionRequestDTO(null, DURATION);

        mockMvc.perform(MockMvcRequestBuilders.post(ApiPaths.SESSIONS)
                                              .contentType(MediaType.APPLICATION_JSON)
                                              .content(objectMapper.writeValueAsString(request)))
               .andExpect(MockMvcResultMatchers.status().isBadRequest());

        Mockito.verify(service, Mockito.never()).open(Mockito.any());
    }

    private SessionResponseDTO buildSessionResponse() {
        LocalDateTime now = LocalDateTime.now();

        return SessionResponseDTO.builder()
                                 .id(SESSION_ID)
                                 .agendaId(AGENDA_ID)
                                 .duration(DURATION)
                                 .startDate(now)
                                 .endDate(now.plusMinutes(DURATION))
                                 .status(SessionStatus.OPEN)
                                 .build();
    }
}
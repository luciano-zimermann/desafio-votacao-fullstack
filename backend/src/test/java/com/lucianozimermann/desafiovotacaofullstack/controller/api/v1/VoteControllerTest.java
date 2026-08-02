package com.lucianozimermann.desafiovotacaofullstack.controller.api.v1;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.lucianozimermann.desafiovotacaofullstack.dto.request.VoteRequestDTO;
import com.lucianozimermann.desafiovotacaofullstack.dto.response.VoteResponseDTO;
import com.lucianozimermann.desafiovotacaofullstack.dto.response.VoteResultResponseDTO;
import com.lucianozimermann.desafiovotacaofullstack.enums.SessionStatus;
import com.lucianozimermann.desafiovotacaofullstack.enums.VoteValue;
import com.lucianozimermann.desafiovotacaofullstack.exception.*;
import com.lucianozimermann.desafiovotacaofullstack.service.VoteService;
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
class VoteControllerTest {

    private static final Long AGENDA_ID = 1L;
    private static final Long SESSION_ID = 1L;
    private static final Long ASSOCIATE_ID = 1L;
    private static final Long VOTE_ID = 1L;

    @Mock
    private VoteService service;

    private MockMvc mockMvc;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    void setup() {
        mockMvc = MockMvcBuilders.standaloneSetup(new VoteController(service))
                                 .setControllerAdvice(new GlobalExceptionHandler())
                                 .build();
    }

    @Test
    @DisplayName("Deve retornar 201 quando o voto for registrado com sucesso")
    void shouldReturn201WhenVoteIsRegistered() throws Exception {
        VoteRequestDTO request = new VoteRequestDTO(SESSION_ID, ASSOCIATE_ID, VoteValue.YES);

        Mockito.when(service.register(Mockito.any(VoteRequestDTO.class))).thenReturn(buildVoteResponse());

        mockMvc.perform(MockMvcRequestBuilders.post(ApiPaths.VOTES)
                                              .contentType(MediaType.APPLICATION_JSON)
                                              .content(objectMapper.writeValueAsString(request)))
               .andExpect(MockMvcResultMatchers.status().isCreated())
               .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(VOTE_ID))
               .andExpect(MockMvcResultMatchers.jsonPath("$.vote").value("YES"));
    }

    @Test
    @DisplayName("Deve retornar 404 quando a sessão não existir")
    void shouldReturn404WhenSessionDoesNotExist() throws Exception {
        VoteRequestDTO request = new VoteRequestDTO(SESSION_ID, ASSOCIATE_ID, VoteValue.YES);

        Mockito.when(service.register(Mockito.any(VoteRequestDTO.class))).thenThrow(new SessionNotFoundException());

        mockMvc.perform(MockMvcRequestBuilders.post(ApiPaths.VOTES)
                                              .contentType(MediaType.APPLICATION_JSON)
                                              .content(objectMapper.writeValueAsString(request)))
               .andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    @Test
    @DisplayName("Deve retornar 404 quando o associado não existir")
    void shouldReturn404WhenAssociateDoesNotExist() throws Exception {
        VoteRequestDTO request = new VoteRequestDTO(SESSION_ID, ASSOCIATE_ID, VoteValue.YES);

        Mockito.when(service.register(Mockito.any(VoteRequestDTO.class))).thenThrow(new AssociateNotFoundException());

        mockMvc.perform(MockMvcRequestBuilders.post(ApiPaths.VOTES)
                                              .contentType(MediaType.APPLICATION_JSON)
                                              .content(objectMapper.writeValueAsString(request)))
               .andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    @Test
    @DisplayName("Deve retornar 409 quando a sessão já estiver encerrada")
    void shouldReturn409WhenSessionIsClosed() throws Exception {
        VoteRequestDTO request = new VoteRequestDTO(SESSION_ID, ASSOCIATE_ID, VoteValue.YES);

        Mockito.when(service.register(Mockito.any(VoteRequestDTO.class))).thenThrow(new SessionClosedException());

        mockMvc.perform(MockMvcRequestBuilders.post(ApiPaths.VOTES)
                                              .contentType(MediaType.APPLICATION_JSON)
                                              .content(objectMapper.writeValueAsString(request)))
               .andExpect(MockMvcResultMatchers.status().isConflict());
    }

    @Test
    @DisplayName("Deve retornar 409 quando o associado já tiver votado na pauta")
    void shouldReturn409WhenAssociateAlreadyVoted() throws Exception {
        VoteRequestDTO request = new VoteRequestDTO(SESSION_ID, ASSOCIATE_ID, VoteValue.YES);

        Mockito.when(service.register(Mockito.any(VoteRequestDTO.class))).thenThrow(new VoteAlreadyExistsException());

        mockMvc.perform(MockMvcRequestBuilders.post(ApiPaths.VOTES)
                                              .contentType(MediaType.APPLICATION_JSON)
                                              .content(objectMapper.writeValueAsString(request)))
               .andExpect(MockMvcResultMatchers.status().isConflict());
    }

    @Test
    @DisplayName("Deve retornar 400 quando o voto não for informado")
    void shouldReturn400WhenVoteIsNull() throws Exception {
        VoteRequestDTO request = new VoteRequestDTO(SESSION_ID, ASSOCIATE_ID, null);

        mockMvc.perform(MockMvcRequestBuilders.post(ApiPaths.VOTES)
                                              .contentType(MediaType.APPLICATION_JSON)
                                              .content(objectMapper.writeValueAsString(request)))
               .andExpect(MockMvcResultMatchers.status().isBadRequest());

        Mockito.verify(service, Mockito.never()).register(Mockito.any());
    }

    @Test
    @DisplayName("Deve retornar 200 com o resultado da apuração da pauta")
    void shouldReturn200WithVoteResult() throws Exception {
        Mockito.when(service.getResult(AGENDA_ID)).thenReturn(buildVoteResult());

        mockMvc.perform(MockMvcRequestBuilders.get(ApiPaths.VOTES + "/results/{agendaId}", AGENDA_ID))
               .andExpect(MockMvcResultMatchers.status().isOk())
               .andExpect(MockMvcResultMatchers.jsonPath("$.result").value("Aprovada"))
               .andExpect(MockMvcResultMatchers.jsonPath("$.totalVotes").value(4))
               .andExpect(MockMvcResultMatchers.jsonPath("$.sessionStatus").value("OPEN"));
    }

    private VoteResponseDTO buildVoteResponse() {
        return VoteResponseDTO.builder()
                              .id(VOTE_ID)
                              .sessionId(SESSION_ID)
                              .associateId(ASSOCIATE_ID)
                              .vote(VoteValue.YES)
                              .build();
    }

    private VoteResultResponseDTO buildVoteResult() {
        return VoteResultResponseDTO.builder()
                                    .agendaId(AGENDA_ID)
                                    .totalVotes(4L)
                                    .yesVotes(3L)
                                    .noVotes(1L)
                                    .result("Aprovada")
                                    .sessionStatus(SessionStatus.OPEN)
                                    .build();
    }
}
package com.lucianozimermann.desafiovotacaofullstack.controller.v1;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.lucianozimermann.desafiovotacaofullstack.dto.request.AssociateRequestDTO;
import com.lucianozimermann.desafiovotacaofullstack.dto.response.AssociateResponseDTO;
import com.lucianozimermann.desafiovotacaofullstack.exception.AssociateAlreadyExistsException;
import com.lucianozimermann.desafiovotacaofullstack.exception.GlobalExceptionHandler;
import com.lucianozimermann.desafiovotacaofullstack.service.AssociateService;
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
class AssociateControllerTest {

    private static final Long ASSOCIATE_ID = 1L;
    private static final String ASSOCIATE_NAME = "Renato Ferreira";
    private static final String ASSOCIATE_CPF = "529.982.247-25";
    private static final String INVALID_CPF = "111.111.111-11";

    @Mock
    private AssociateService service;

    private MockMvc mockMvc;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    void setup() {
        mockMvc = MockMvcBuilders.standaloneSetup(new AssociateController(service))
                                 .setControllerAdvice(new GlobalExceptionHandler())
                                 .build();
    }

    @Test
    @DisplayName("Deve retornar 201 quando os dados do associado forem válidos")
    void shouldReturn201WhenRequestIsValid() throws Exception {
        AssociateRequestDTO request = new AssociateRequestDTO(ASSOCIATE_NAME, ASSOCIATE_CPF);

        Mockito.when(service.create(Mockito.any(AssociateRequestDTO.class))).thenReturn(buildAssociateResponse());

        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/associates")
                                              .contentType(MediaType.APPLICATION_JSON)
                                              .content(objectMapper.writeValueAsString(request)))
               .andExpect(MockMvcResultMatchers.status().isCreated())
               .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(ASSOCIATE_ID))
               .andExpect(MockMvcResultMatchers.jsonPath("$.name").value(ASSOCIATE_NAME));
    }

    @Test
    @DisplayName("Deve retornar 409 quando o CPF já estiver cadastrado")
    void shouldReturn409WhenCpfAlreadyExists() throws Exception {
        AssociateRequestDTO request = new AssociateRequestDTO(ASSOCIATE_NAME, ASSOCIATE_CPF);

        Mockito.when(service.create(Mockito.any(AssociateRequestDTO.class))).thenThrow(new AssociateAlreadyExistsException());

        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/associates")
                                              .contentType(MediaType.APPLICATION_JSON)
                                              .content(objectMapper.writeValueAsString(request)))
               .andExpect(MockMvcResultMatchers.status().isConflict());
    }

    @Test
    @DisplayName("Deve retornar 400 quando o CPF for inválido")
    void shouldReturn400WhenCpfIsInvalid() throws Exception {
        AssociateRequestDTO request = new AssociateRequestDTO(ASSOCIATE_NAME, INVALID_CPF);

        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/associates")
                                              .contentType(MediaType.APPLICATION_JSON)
                                              .content(objectMapper.writeValueAsString(request)))
               .andExpect(MockMvcResultMatchers.status().isBadRequest());

        Mockito.verify(service, Mockito.never()).create(Mockito.any());
    }

    @Test
    @DisplayName("Deve retornar 400 quando o nome estiver em branco")
    void shouldReturn400WhenNameIsBlank() throws Exception {
        AssociateRequestDTO request = new AssociateRequestDTO("", ASSOCIATE_CPF);

        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/associates")
                                              .contentType(MediaType.APPLICATION_JSON)
                                              .content(objectMapper.writeValueAsString(request)))
               .andExpect(MockMvcResultMatchers.status().isBadRequest());

        Mockito.verify(service, Mockito.never()).create(Mockito.any());
    }

    private AssociateResponseDTO buildAssociateResponse() {
        return AssociateResponseDTO.builder()
                                   .id(ASSOCIATE_ID)
                                   .name(ASSOCIATE_NAME)
                                   .cpf(ASSOCIATE_CPF)
                                   .build();
    }
}
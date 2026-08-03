package com.lucianozimermann.desafiovotacaofullstack.client.controller.api.v1;

import com.lucianozimermann.desafiovotacaofullstack.client.enums.CPFVoteStatus;
import com.lucianozimermann.desafiovotacaofullstack.client.exception.CPFNotFoundException;
import com.lucianozimermann.desafiovotacaofullstack.client.service.CPFValidatorService;
import com.lucianozimermann.desafiovotacaofullstack.exception.GlobalExceptionHandler;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

@ExtendWith(MockitoExtension.class)
class CPFValidatorControllerTest {

    private static final String BASE_PATH = "/client/cpf/validate/{cpf}";
    private static final String VALID_CPF = "52998224725";
    private static final String INVALID_CPF = "11111111111";

    @Mock
    private CPFValidatorService service;

    private MockMvc mockMvc;

    @BeforeEach
    void setup() {
        mockMvc = MockMvcBuilders.standaloneSetup(new CPFValidatorController(service))
                                 .setControllerAdvice(new GlobalExceptionHandler())
                                 .build();
    }

    @Test
    @DisplayName("Deve retornar 200 com status ABLE_TO_VOTE quando o CPF puder votar")
    void shouldReturn200WithAbleToVoteStatus() throws Exception {
        Mockito.when(service.validate(VALID_CPF)).thenReturn(CPFVoteStatus.ABLE_TO_VOTE);

        mockMvc.perform(MockMvcRequestBuilders.get(BASE_PATH, VALID_CPF))
               .andExpect(MockMvcResultMatchers.status().isOk())
               .andExpect(MockMvcResultMatchers.jsonPath("$.status").value("ABLE_TO_VOTE"));
    }

    @Test
    @DisplayName("Deve retornar 200 com status UNABLE_TO_VOTE quando o CPF não puder votar")
    void shouldReturn200WithUnableToVoteStatus() throws Exception {
        Mockito.when(service.validate(VALID_CPF)).thenReturn(CPFVoteStatus.UNABLE_TO_VOTE);

        mockMvc.perform(MockMvcRequestBuilders.get(BASE_PATH, VALID_CPF))
               .andExpect(MockMvcResultMatchers.status().isOk())
               .andExpect(MockMvcResultMatchers.jsonPath("$.status").value("UNABLE_TO_VOTE"));
    }

    @Test
    @DisplayName("Deve retornar 404 quando o CPF não for encontrado")
    void shouldReturn404WhenCpfIsNotFound() throws Exception {
        Mockito.when(service.validate(INVALID_CPF)).thenThrow(new CPFNotFoundException("CPF não encontrado"));

        mockMvc.perform(MockMvcRequestBuilders.get(BASE_PATH, INVALID_CPF))
               .andExpect(MockMvcResultMatchers.status().isNotFound());
    }
}
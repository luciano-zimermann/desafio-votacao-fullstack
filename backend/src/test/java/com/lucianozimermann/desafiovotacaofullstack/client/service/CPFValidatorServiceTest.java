package com.lucianozimermann.desafiovotacaofullstack.client.service;

import com.lucianozimermann.desafiovotacaofullstack.client.enums.CPFVoteStatus;
import com.lucianozimermann.desafiovotacaofullstack.client.exception.CPFNotFoundException;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class CPFValidatorServiceTest {

    private static final String VALID_CPF   = "52998224725";
    private static final String INVALID_CPF = "11111111111";

    @InjectMocks
    private CPFValidatorService service;

    @Test
    @DisplayName("Deve retornar status de votação para CPF válido")
    void shouldReturnVoteStatusWhenCpfIsValid() {
        CPFVoteStatus result = service.validate(VALID_CPF);

        Assertions.assertThat(result).isIn(CPFVoteStatus.ABLE_TO_VOTE, CPFVoteStatus.UNABLE_TO_VOTE);
    }

    @Test
    @DisplayName("Deve lançar exceção quando CPF não for encontrado")
    void shouldThrowExceptionWhenCpfIsInvalid() {
        Assertions.assertThatThrownBy(() -> service.validate(INVALID_CPF)).isInstanceOf(CPFNotFoundException.class);
    }
}
package com.lucianozimermann.desafiovotacaofullstack.validation;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class CpfValidatorTest {

    private static final String VALID_CPF = "52998224725";
    private static final String VALID_CPF_FORMATTED = "529.982.247-25";
    private static final String INVALID_CHECK_DIGIT_CPF = "52998224726";
    private static final String ALL_DIGITS_EQUAL_CPF = "11111111111";
    private static final String WRONG_LENGTH_CPF = "123456789";

    private final CpfValidator validator = new CpfValidator();

    @Test
    @DisplayName("Deve considerar válido um CPF com dígitos verificadores corretos")
    void shouldReturnTrueForValidCpf() {
        Assertions.assertThat(validator.isValid(VALID_CPF, null)).isTrue();
    }

    @Test
    @DisplayName("Deve considerar válido um CPF formatado com pontuação")
    void shouldReturnTrueForFormattedValidCpf() {
        Assertions.assertThat(validator.isValid(VALID_CPF_FORMATTED, null)).isTrue();
    }

    @Test
    @DisplayName("Deve considerar inválido um CPF com dígito verificador incorreto")
    void shouldReturnFalseForInvalidCheckDigit() {
        Assertions.assertThat(validator.isValid(INVALID_CHECK_DIGIT_CPF, null)).isFalse();
    }

    @Test
    @DisplayName("Deve considerar inválido um CPF com todos os dígitos iguais")
    void shouldReturnFalseWhenAllDigitsAreEqual() {
        Assertions.assertThat(validator.isValid(ALL_DIGITS_EQUAL_CPF, null)).isFalse();
    }

    @Test
    @DisplayName("Deve considerar inválido um CPF com quantidade de dígitos incorreta")
    void shouldReturnFalseForWrongLength() {
        Assertions.assertThat(validator.isValid(WRONG_LENGTH_CPF, null)).isFalse();
    }

    @Test
    @DisplayName("Deve considerar válido um CPF nulo, delegando obrigatoriedade para @NotBlank")
    void shouldReturnTrueForNull() {
        Assertions.assertThat(validator.isValid(null, null)).isTrue();
    }
}
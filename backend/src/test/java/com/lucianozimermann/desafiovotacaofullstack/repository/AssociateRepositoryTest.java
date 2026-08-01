package com.lucianozimermann.desafiovotacaofullstack.repository;

import com.lucianozimermann.desafiovotacaofullstack.entity.Associate;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class AssociateRepositoryTest {

    private static final String ASSOCIATE_NAME = "Renato Ferreira";
    private static final String ASSOCIATE_CPF = "52998224725";

    @Autowired
    private AssociateRepository repository;

    @Test
    @DisplayName("Deve retornar true quando já existir associado com o CPF informado")
    void shouldReturnTrueWhenCpfAlreadyExists() {
        repository.save(buildAssociate());

        boolean exists = repository.existsByCpf(ASSOCIATE_CPF);

        assertThat(exists).isTrue();
    }

    @Test
    @DisplayName("Deve retornar false quando não existir associado com o CPF informado")
    void shouldReturnFalseWhenCpfDoesNotExist() {
        boolean exists = repository.existsByCpf(ASSOCIATE_CPF);

        assertThat(exists).isFalse();
    }

    private Associate buildAssociate() {
        return Associate.builder()
                        .name(ASSOCIATE_NAME)
                        .cpf(ASSOCIATE_CPF)
                        .build();
    }
}
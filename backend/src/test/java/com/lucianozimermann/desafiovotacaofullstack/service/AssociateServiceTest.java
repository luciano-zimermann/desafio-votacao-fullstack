package com.lucianozimermann.desafiovotacaofullstack.service;

import com.lucianozimermann.desafiovotacaofullstack.dto.request.AssociateRequestDTO;
import com.lucianozimermann.desafiovotacaofullstack.dto.response.AssociateResponseDTO;
import com.lucianozimermann.desafiovotacaofullstack.entity.Associate;
import com.lucianozimermann.desafiovotacaofullstack.exception.AssociateAlreadyExistsException;
import com.lucianozimermann.desafiovotacaofullstack.exception.AssociateNotFoundException;
import com.lucianozimermann.desafiovotacaofullstack.repository.AssociateRepository;
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
class AssociateServiceTest {

    private static final Long ASSOCIATE_ID = 1L;
    private static final String ASSOCIATE_NAME = "Renato Ferreira";
    private static final String ASSOCIATE_CPF_FORMATTED = "529.982.247-25";
    private static final String ASSOCIATE_CPF_NORMALIZED = "52998224725";

    @Mock
    private AssociateRepository repository;

    @InjectMocks
    private AssociateService service;

    @Test
    @DisplayName("Deve criar um associado normalizando o CPF antes de persistir")
    void shouldCreateAssociateWithNormalizedCpf() {
        AssociateRequestDTO request = new AssociateRequestDTO(ASSOCIATE_NAME, ASSOCIATE_CPF_FORMATTED);

        Mockito.when(repository.existsByCpf(ASSOCIATE_CPF_NORMALIZED)).thenReturn(false);
        Mockito.when(repository.save(Mockito.any(Associate.class))).thenReturn(buildAssociate());

        AssociateResponseDTO response = service.register(request);

        Assertions.assertThat(response).isNotNull();
        Assertions.assertThat(response.id()).isEqualTo(ASSOCIATE_ID);
        Assertions.assertThat(response.name()).isEqualTo(ASSOCIATE_NAME);
        Assertions.assertThat(response.cpf()).isEqualTo(ASSOCIATE_CPF_NORMALIZED);

        Mockito.verify(repository, Mockito.times(1)).existsByCpf(ASSOCIATE_CPF_NORMALIZED);
        Mockito.verify(repository, Mockito.times(1)).save(Mockito.any(Associate.class));
    }

    @Test
    @DisplayName("Deve retornar o associado pelo id")
    void shouldFindAssociateById() {
        Mockito.when(repository.findById(ASSOCIATE_ID)).thenReturn(Optional.of( buildAssociate()));

        AssociateResponseDTO response = service.findById(ASSOCIATE_ID);

        Assertions.assertThat(response.id()).isEqualTo(ASSOCIATE_ID);
        Assertions.assertThat(response.name()).isEqualTo(ASSOCIATE_NAME);
    }

    @Test
    @DisplayName("Deve lançar exceção quando o associado não existir ao buscar por id")
    void shouldThrowExceptionWhenAssociateDoesNotExistOnFindById() {
        Mockito.when(repository.findById(ASSOCIATE_ID)).thenReturn(Optional.empty());

        Assertions.assertThatThrownBy(() -> service.findById(ASSOCIATE_ID)).isInstanceOf(AssociateNotFoundException.class);
    }

    @Test
    @DisplayName("Deve retornar a lista de todos os associados cadastrados")
    void shouldReturnAllAssociates() {
        Mockito.when(repository.findAll()).thenReturn(List.of(buildAssociate()));

        List<AssociateResponseDTO> response = service.findAll();

        Assertions.assertThat(response).hasSize(1);
        Assertions.assertThat(response.getFirst().id()).isEqualTo(ASSOCIATE_ID);
    }

    @Test
    @DisplayName("Deve lançar exceção quando o CPF já estiver cadastrado")
    void shouldThrowExceptionWhenCpfAlreadyExists() {
        AssociateRequestDTO request = new AssociateRequestDTO(ASSOCIATE_NAME, ASSOCIATE_CPF_FORMATTED);

        Mockito.when(repository.existsByCpf(ASSOCIATE_CPF_NORMALIZED)).thenReturn(true);

        Assertions.assertThatThrownBy(() -> service.register(request)).isInstanceOf(AssociateAlreadyExistsException.class);

        Mockito.verify(repository, Mockito.never()).save(Mockito.any(Associate.class));
    }

    private Associate buildAssociate() {
        return Associate.builder()
                        .id(ASSOCIATE_ID)
                        .name(ASSOCIATE_NAME)
                        .cpf(ASSOCIATE_CPF_NORMALIZED)
                        .build();
    }
}
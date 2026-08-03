package com.lucianozimermann.desafiovotacaofullstack.service;

import com.lucianozimermann.desafiovotacaofullstack.dto.request.AssociateRequestDTO;
import com.lucianozimermann.desafiovotacaofullstack.dto.response.AssociateResponseDTO;
import com.lucianozimermann.desafiovotacaofullstack.entity.Associate;
import com.lucianozimermann.desafiovotacaofullstack.exception.AssociateAlreadyExistsException;
import com.lucianozimermann.desafiovotacaofullstack.exception.AssociateNotFoundException;
import com.lucianozimermann.desafiovotacaofullstack.repository.AssociateRepository;
import com.lucianozimermann.desafiovotacaofullstack.utils.CpfUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class AssociateService {

    private final AssociateRepository repository;

    public AssociateResponseDTO findById(Long id) {
        Associate associate = repository.findById(id)
                                        .orElseThrow(() -> {
                                            log.warn("Tenativa de buscar uma associado inexistente. id={}", id);
                                            return new AssociateNotFoundException();
                                        });

        return buildAssociateResponseDTO(associate);
    }

    public List<AssociateResponseDTO> findAll() {
        List<AssociateResponseDTO> associates = repository.findAll()
                                                          .stream()
                                                          .map(this::buildAssociateResponseDTO)
                                                          .toList();

        log.info("Listagem de associados retornadas. total={}", associates.size());

        return associates;
    }

    public AssociateResponseDTO register(AssociateRequestDTO dto) {
        String cpf = CpfUtils.stripCpfMask(dto.cpf());

        if (repository.existsByCpf(cpf)) {
            log.warn("Tentativa de cadastrar um associado com CPF já existente.");
            throw new AssociateAlreadyExistsException();
        }

        Associate associate = Associate.builder()
                                       .name(dto.name())
                                       .cpf(cpf)
                                       .build();

        associate = repository.save(associate);

        log.info("Associado cadastrado com sucesso. id={}", associate.getId());

        return buildAssociateResponseDTO(associate);
    }

    private AssociateResponseDTO buildAssociateResponseDTO(Associate associate) {
        return AssociateResponseDTO.builder()
                                   .id(associate.getId())
                                   .name(associate.getName())
                                   .cpf(associate.getCpf())
                                   .build();
    }
}
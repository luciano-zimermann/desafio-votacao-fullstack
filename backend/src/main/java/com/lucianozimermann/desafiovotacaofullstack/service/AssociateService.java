package com.lucianozimermann.desafiovotacaofullstack.service;

import com.lucianozimermann.desafiovotacaofullstack.dto.request.AssociateRequestDTO;
import com.lucianozimermann.desafiovotacaofullstack.dto.response.AssociateResponseDTO;
import com.lucianozimermann.desafiovotacaofullstack.entity.Associate;
import com.lucianozimermann.desafiovotacaofullstack.exception.AssociateAlreadyExistsException;
import com.lucianozimermann.desafiovotacaofullstack.repository.AssociateRepository;
import com.lucianozimermann.desafiovotacaofullstack.utils.CpfUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AssociateService {

    private final AssociateRepository repository;

    public AssociateResponseDTO create(AssociateRequestDTO dto) {
        String cpf = CpfUtils.stripCpfMask(dto.cpf());

        if (repository.existsByCpf(cpf)) {
            throw new AssociateAlreadyExistsException();
        }

        Associate associate = Associate.builder()
                                       .name(dto.name())
                                       .cpf(cpf)
                                       .build();

        associate = repository.save(associate);

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
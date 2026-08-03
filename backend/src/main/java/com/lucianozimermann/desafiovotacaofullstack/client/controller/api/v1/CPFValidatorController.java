package com.lucianozimermann.desafiovotacaofullstack.client.controller.api.v1;

import com.lucianozimermann.desafiovotacaofullstack.client.dto.response.CPFResponseDTO;
import com.lucianozimermann.desafiovotacaofullstack.client.enums.CPFVoteStatus;
import com.lucianozimermann.desafiovotacaofullstack.client.service.CPFValidatorService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/client/cpf")
@RequiredArgsConstructor
public class CPFValidatorController {

    private final CPFValidatorService cpfValidatorService;

    @GetMapping("/validate/{cpf}")
    public ResponseEntity<CPFResponseDTO> validateCpf(@PathVariable String cpf) {
        CPFVoteStatus status = cpfValidatorService.validate(cpf);

        return ResponseEntity.ok(CPFResponseDTO.builder()
                                               .status(status.name())
                                               .build());
    }
}
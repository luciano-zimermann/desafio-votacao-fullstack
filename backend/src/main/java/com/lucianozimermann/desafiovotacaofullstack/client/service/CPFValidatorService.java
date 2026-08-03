package com.lucianozimermann.desafiovotacaofullstack.client.service;

import com.lucianozimermann.desafiovotacaofullstack.client.enums.CPFVoteStatus;
import com.lucianozimermann.desafiovotacaofullstack.client.exception.CPFNotFoundException;
import com.lucianozimermann.desafiovotacaofullstack.utils.CpfUtils;
import org.springframework.stereotype.Service;

import java.util.concurrent.ThreadLocalRandom;

@Service
public class CPFValidatorService {

    public CPFVoteStatus validate(String cpf) {
        if (!isValid(cpf)) {
            throw new CPFNotFoundException("CPF não encontrado");
        }

        boolean canVote = ThreadLocalRandom.current().nextBoolean();

        return canVote ? CPFVoteStatus.ABLE_TO_VOTE
                       : CPFVoteStatus.UNABLE_TO_VOTE;
    }

    public boolean isValid(String cpf) {
        if (cpf == null) {
            return true;
        }

        cpf = CpfUtils.stripCpfMask( cpf);

        if (cpf.length() != 11 || cpf.chars().distinct().count() == 1) {
            return false;
        }

        return validateDigit(cpf, 9) && validateDigit(cpf, 10);
    }

    private boolean validateDigit(String cpf, int position) {
        int sum = 0;

        for (int i = 0; i < position; i++) {
            sum += Character.getNumericValue(cpf.charAt(i)) * (position + 1 - i);
        }

        int remainder = sum % 11;
        int digit = remainder < 2 ? 0 : 11 - remainder;

        return digit == Character.getNumericValue(cpf.charAt(position));
    }
}
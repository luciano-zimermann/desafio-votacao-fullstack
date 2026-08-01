package com.lucianozimermann.desafiovotacaofullstack.validation;

import com.lucianozimermann.desafiovotacaofullstack.utils.CpfUtils;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class CpfValidator implements ConstraintValidator<ValidCpf, String> {

    @Override
    public boolean isValid(String cpf, ConstraintValidatorContext context) {
        if (cpf == null) {
            return true;
        }

        cpf = CpfUtils.stripCpfMask(cpf);

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
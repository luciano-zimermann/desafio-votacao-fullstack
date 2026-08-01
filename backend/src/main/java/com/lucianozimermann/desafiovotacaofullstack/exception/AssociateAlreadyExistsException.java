package com.lucianozimermann.desafiovotacaofullstack.exception;

public class AssociateAlreadyExistsException extends RuleConflictException {
    public AssociateAlreadyExistsException() {
        super("Já existe um associado cadastrado com esse CPF!");
    }
}
package com.lucianozimermann.desafiovotacaofullstack.exception;

public class AssociateNotFoundException extends EntityNotFoundException {

    public AssociateNotFoundException() {
        super("Associado não encontrado!");
    }
}
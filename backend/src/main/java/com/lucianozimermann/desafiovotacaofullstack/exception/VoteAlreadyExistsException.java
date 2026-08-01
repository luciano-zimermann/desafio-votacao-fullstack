package com.lucianozimermann.desafiovotacaofullstack.exception;

public class VoteAlreadyExistsException extends RuleConflictException {

    public VoteAlreadyExistsException() {
        super("Associado já votou nessa pauta!");
    }
}
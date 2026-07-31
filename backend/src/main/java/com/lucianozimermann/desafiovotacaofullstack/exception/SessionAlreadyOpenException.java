package com.lucianozimermann.desafiovotacaofullstack.exception;

public class SessionAlreadyOpenException extends RuleConflictException {
    public SessionAlreadyOpenException() {
        super("Já existe uma Sessão aberta para essa Pauta!");
    }
}
package com.lucianozimermann.desafiovotacaofullstack.exception;

public class SessionClosedException extends RuleConflictException {

    public SessionClosedException() {
        super("Sessão de votação encerrada!");
    }
}
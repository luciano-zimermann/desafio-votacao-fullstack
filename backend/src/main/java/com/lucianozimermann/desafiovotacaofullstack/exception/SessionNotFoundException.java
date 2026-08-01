package com.lucianozimermann.desafiovotacaofullstack.exception;

public class SessionNotFoundException extends EntityNotFoundException {

    public SessionNotFoundException() {
        super("Sessão não encontrada!");
    }
}
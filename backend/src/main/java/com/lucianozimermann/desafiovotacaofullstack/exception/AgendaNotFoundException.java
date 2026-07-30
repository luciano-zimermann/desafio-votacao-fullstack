package com.lucianozimermann.desafiovotacaofullstack.exception;


public class AgendaNotFoundException extends EntityNotFoundException {

    public AgendaNotFoundException() {
        super("Pauta não encontrada!");
    }
}
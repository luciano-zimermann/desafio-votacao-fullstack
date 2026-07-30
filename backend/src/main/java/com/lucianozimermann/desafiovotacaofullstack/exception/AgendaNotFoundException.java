package com.lucianozimermann.desafiovotacaofullstack.exception;


public class AgendaNotFoundException extends RuntimeException {

    public AgendaNotFoundException() {
        super("Pauta não encontrada!");
    }
}
package com.lucianozimermann.desafiovotacaofullstack.exception;

public class RuleConflictException extends RuntimeException {
    public RuleConflictException(String message) {
        super(message);
    }
}
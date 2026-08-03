package com.lucianozimermann.desafiovotacaofullstack.client.exception;

import com.lucianozimermann.desafiovotacaofullstack.exception.EntityNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class CPFNotFoundException extends EntityNotFoundException {
    public CPFNotFoundException(String message) {
        super(message);
    }
}
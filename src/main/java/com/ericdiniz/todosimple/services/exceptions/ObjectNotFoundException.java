package com.ericdiniz.todosimple.services.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND)
public class ObjectNotFoundException extends jakarta.persistence.EntityNotFoundException {

    public ObjectNotFoundException(String message) {
        super(message);
    }

}
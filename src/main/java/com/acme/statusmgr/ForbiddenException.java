package com.acme.statusmgr;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.server.ResponseStatusException;

/**
 * Class used to generate custom message if certain requirements are not met.
 */

@ResponseStatus(value = HttpStatus.BAD_REQUEST)
public class ForbiddenException extends ResponseStatusException {

    public ForbiddenException(String errorMessage) {
        super(HttpStatus.BAD_REQUEST, errorMessage);
    }
}

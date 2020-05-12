package io.kagboton.covidtracker.coviddeathsservice.services.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class CountryNotFoundException extends RuntimeException {

    public CountryNotFoundException(String s) {
        super(s);
    }

    public CountryNotFoundException() {
    }
}

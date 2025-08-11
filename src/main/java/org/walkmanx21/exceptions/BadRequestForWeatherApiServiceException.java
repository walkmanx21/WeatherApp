package org.walkmanx21.exceptions;

public class BadRequestForWeatherApiServiceException extends RuntimeException {
    public BadRequestForWeatherApiServiceException(String message) {
        super(message);
    }
}

package org.walkmanx21.exceptions;

public class WeatherApiServiceUnavailableException extends RuntimeException {
    public WeatherApiServiceUnavailableException(String message) {
        super(message);
    }
}

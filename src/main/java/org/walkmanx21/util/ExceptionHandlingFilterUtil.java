package org.walkmanx21.util;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.ModelAndView;
import org.walkmanx21.dto.ErrorResponseDto;
import org.walkmanx21.exceptions.BadRequestForWeatherApiServiceException;
import org.walkmanx21.exceptions.LocationAlreadyExistException;
import org.walkmanx21.exceptions.StorageUnavailableException;
import org.walkmanx21.exceptions.WeatherApiServiceUnavailableException;

@ControllerAdvice
public class ExceptionHandlingFilterUtil {

    @ExceptionHandler({
            StorageUnavailableException.class,
            BadRequestForWeatherApiServiceException.class,
            WeatherApiServiceUnavailableException.class
    })
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ModelAndView handleExceptionsForRedirectView (Exception e) {
        return new ModelAndView("/error");
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ModelAndView handleException(Exception e) {
        return new ModelAndView("/error");
    }
}

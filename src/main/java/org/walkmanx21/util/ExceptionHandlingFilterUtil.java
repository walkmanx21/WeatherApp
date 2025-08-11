package org.walkmanx21.util;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.view.RedirectView;
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
    public RedirectView handleExceptionsForRedirectView (Exception e) {
        return new RedirectView("/error");
    }

    @ExceptionHandler(LocationAlreadyExistException.class)
    public ResponseEntity<ErrorResponseDto> handleLocationAlreadyException(LocationAlreadyExistException e) {
        ErrorResponseDto errorResponseDto = new ErrorResponseDto(HttpStatus.CONFLICT, e.getMessage());
        return new ResponseEntity<>(errorResponseDto, HttpStatus.CONFLICT);
    }
}

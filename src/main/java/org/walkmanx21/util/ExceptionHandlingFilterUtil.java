package org.walkmanx21.util;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.ModelAndView;
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
        return buildModelAndView("error", "We're sorry, but an unexpected error has occurred. Please try again later.");
    }

//    @ExceptionHandler(Exception.class)
//    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
//    public ModelAndView handleException(Exception e) {
//        return buildModelAndView("error", "The requested page was not found");
//    }

    private ModelAndView buildModelAndView(String viewName, String message) {
        ModelAndView modelAndView = new ModelAndView(viewName);
        modelAndView.addObject("message", message);
        return modelAndView;
    }
}

package org.walkmanx21.util;

import org.hibernate.exception.ConstraintViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.walkmanx21.dto.ErrorResponseDto;
import org.walkmanx21.exceptions.UserAlreadyExistException;

@ControllerAdvice
public class ExceptionHandlingFilterUtil {

//    @ExceptionHandler(ConstraintViolationException.class)
//    public void handleConstraintViolationException (ConstraintViolationException e) {
//        throw new UserAlreadyExistException("User with this username already exists");
//    }

}

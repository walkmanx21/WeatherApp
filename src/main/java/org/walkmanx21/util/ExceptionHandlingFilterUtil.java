package org.walkmanx21.util;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.walkmanx21.dto.ErrorResponseDto;
import org.walkmanx21.exceptions.StorageUnavailableException;

@RestControllerAdvice
public class ExceptionHandlingFilterUtil {

    @ExceptionHandler(StorageUnavailableException.class)
    public ResponseEntity<ErrorResponseDto> handleStorageUnavailableException(StorageUnavailableException e) {
        ErrorResponseDto errorResponseDto = new ErrorResponseDto(
                HttpStatus.INTERNAL_SERVER_ERROR,
                e.getMessage()
        );
        return new ResponseEntity<>(errorResponseDto, HttpStatus.INTERNAL_SERVER_ERROR);
    }

}

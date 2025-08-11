package org.walkmanx21.util;

import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import org.walkmanx21.dto.UserRequestDto;

@Component
public class UserRequestDtoValidatorUtil implements Validator {

    @Override
    public boolean supports(Class<?> clazz) {
        return UserRequestDto.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        UserRequestDto userRequestDto = (UserRequestDto) target;

        if (!userRequestDto.getPassword().equals(userRequestDto.getRepeatPassword()) && userRequestDto.getRepeatPassword() != null)
            errors.rejectValue("repeatPassword", "", "Passwords don't match");
    }
}

package org.walkmanx21.util;

import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import org.walkmanx21.dto.UserRequestDto;

@Component
public class UserRequestDtoValidator implements Validator {
    @Override
    public boolean supports(Class<?> clazz) {
        return UserRequestDto.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        UserRequestDto userRequestDto = (UserRequestDto) target;

        if (!userRequestDto.getPassword().equals(userRequestDto.getRepeatPassword()))
            errors.rejectValue("repeatPassword", "400", "Passwords don't match");
    }
}

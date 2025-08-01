package org.walkmanx21.controllers;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.walkmanx21.dao.UserDao;
import org.walkmanx21.dto.UserRequestDto;
import org.walkmanx21.exceptions.UserAlreadyExistException;
import org.walkmanx21.services.UserService;
import org.walkmanx21.util.UserRequestDtoValidator;

@Controller("/sign-up")
@RequestMapping("/sign-up")
public class SignUpController {

    private final UserRequestDtoValidator userRequestDtoValidator;
    private final UserService userService;

    @Autowired
    public SignUpController(UserDao userDao, UserRequestDtoValidator userRequestDtoValidator, UserService userService) {
        this.userRequestDtoValidator = userRequestDtoValidator;
        this.userService = userService;
    }

    @GetMapping
    public String newUserRequestDto(@ModelAttribute("userRequestDto") UserRequestDto userRequestDto) {
        return "sign-up/sign-up";
    }

    @PostMapping
    public String signUp(@ModelAttribute("userRequestDto") @Valid UserRequestDto userRequestDto, BindingResult bindingResult) {

        userRequestDtoValidator.validate(userRequestDto, bindingResult);

        if (bindingResult.hasErrors())
            return "sign-up/sign-up-with-errors";

        try {
            userService.insertUser(userRequestDto);
        } catch (UserAlreadyExistException e) {
            bindingResult.rejectValue("username", "", e.getMessage());
            return "sign-up/sign-up-with-errors";
        }

        return "redirect:/";
    }
}

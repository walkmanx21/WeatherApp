package org.walkmanx21.controllers;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.walkmanx21.dao.UserDao;
import org.walkmanx21.dto.UserRequestDto;
import org.walkmanx21.util.UserRequestDtoValidator;

@Controller("/sign-up")
@RequestMapping("/sign-up")
public class SignUpController {

    private final UserDao userDao;
    private final UserRequestDtoValidator userRequestDtoValidator;

    @Autowired
    public SignUpController(UserDao userDao, UserRequestDtoValidator userRequestDtoValidator) {
        this.userDao = userDao;
        this.userRequestDtoValidator = userRequestDtoValidator;
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

        userDao.insertUser(userRequestDto);
        return "redirect:/index";
    }
}

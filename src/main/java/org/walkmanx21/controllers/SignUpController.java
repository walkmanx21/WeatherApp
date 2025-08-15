package org.walkmanx21.controllers;

import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.walkmanx21.dto.UserRequestDto;
import org.walkmanx21.dto.UserResponseDto;
import org.walkmanx21.exceptions.UserAlreadyExistException;
import org.walkmanx21.services.UserService;
import org.walkmanx21.util.CookieUtil;
import org.walkmanx21.util.UserRequestDtoValidatorUtil;

@Controller("/sign-up")
@RequestMapping("/sign-up")
public class SignUpController {

    private final UserRequestDtoValidatorUtil userRequestDtoValidatorUtil;
    private final UserService userService;
    private final CookieUtil createCookieUtil;

    @Autowired
    public SignUpController(UserRequestDtoValidatorUtil userRequestDtoValidatorUtil, UserService userService, CookieUtil createCookieUtil) {
        this.userRequestDtoValidatorUtil = userRequestDtoValidatorUtil;
        this.userService = userService;
        this.createCookieUtil = createCookieUtil;
    }

    @GetMapping
    public String newUserRequestDto(@ModelAttribute("userRequestDto") UserRequestDto userRequestDto) {
        return "sign-up/sign-up";
    }

    @PostMapping
    public String signUp(@ModelAttribute("userRequestDto") @Valid UserRequestDto userRequestDto, BindingResult bindingResult, HttpServletResponse response) {

        userRequestDtoValidatorUtil.validate(userRequestDto, bindingResult);

        if (bindingResult.hasErrors())
            return "sign-up/sign-up-with-errors";

        try {
            UserResponseDto userResponseDto = userService.registerUser(userRequestDto);
            createCookieUtil.setSessionId(userResponseDto, response);
        } catch (UserAlreadyExistException e) {
            bindingResult.rejectValue("login", "", e.getMessage());
            return "sign-up/sign-up-with-errors";
        }

        return "redirect:/";
    }
}

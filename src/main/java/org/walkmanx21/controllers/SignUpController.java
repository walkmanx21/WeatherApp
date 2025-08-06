package org.walkmanx21.controllers;

import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.walkmanx21.dao.UserDao;
import org.walkmanx21.dto.UserRequestDto;
import org.walkmanx21.dto.UserResponseDto;
import org.walkmanx21.exceptions.UserAlreadyExistException;
import org.walkmanx21.services.UserService;
import org.walkmanx21.util.CreateCookieUtil;
import org.walkmanx21.util.SetSessionAttributesUtil;
import org.walkmanx21.util.UserRequestDtoValidator;

@Controller("/sign-up")
@RequestMapping("/sign-up")
public class SignUpController {

    private final UserRequestDtoValidator userRequestDtoValidator;
    private final UserService userService;
    private final SetSessionAttributesUtil setSessionAttributesUtil;
    private final CreateCookieUtil createCookieUtil;

    @Autowired
    public SignUpController(UserDao userDao, UserRequestDtoValidator userRequestDtoValidator, UserService userService, SetSessionAttributesUtil setSessionAttributesUtil, CreateCookieUtil createCookieUtil) {
        this.userRequestDtoValidator = userRequestDtoValidator;
        this.userService = userService;
        this.setSessionAttributesUtil = setSessionAttributesUtil;
        this.createCookieUtil = createCookieUtil;
    }

    @GetMapping
    public String newUserRequestDto(@ModelAttribute("userRequestDto") UserRequestDto userRequestDto) {
        return "sign-up/sign-up";
    }

    @PostMapping
    public String signUp(@ModelAttribute("userRequestDto") @Valid UserRequestDto userRequestDto, BindingResult bindingResult, HttpSession httpSession, HttpServletResponse response) {

        userRequestDtoValidator.validate(userRequestDto, bindingResult);

        if (bindingResult.hasErrors())
            return "sign-up/sign-up-with-errors";

        try {
            UserResponseDto userResponseDto = userService.registerUser(userRequestDto);
            setSessionAttributesUtil.setSessionAttributes(httpSession, userResponseDto);
            response.addCookie(createCookieUtil.createCookie(userResponseDto));
        } catch (UserAlreadyExistException e) {
            bindingResult.rejectValue("login", "", e.getMessage());
            return "sign-up/sign-up-with-errors";
        }

        return "redirect:/";
    }
}

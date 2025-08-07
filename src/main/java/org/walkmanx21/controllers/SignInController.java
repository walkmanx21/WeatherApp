package org.walkmanx21.controllers;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.walkmanx21.dto.UserRequestDto;
import org.walkmanx21.dto.UserResponseDto;
import org.walkmanx21.exceptions.UserDoesNotExistException;
import org.walkmanx21.exceptions.WrongPasswordException;
import org.walkmanx21.services.SessionService;
import org.walkmanx21.services.UserService;
import org.walkmanx21.util.CreateCookieUtil;
import org.walkmanx21.util.SetSessionAttributesUtil;
import org.walkmanx21.util.UserRequestDtoValidator;

import java.util.Optional;

@Controller("/sign-in")
@RequestMapping("/sign-in")
public class SignInController {

    private final UserRequestDtoValidator userRequestDtoValidator;
    private final UserService userService;
    private final SetSessionAttributesUtil setSessionAttributesUtil;
    private final CreateCookieUtil createCookieUtil;

    @Autowired
    public SignInController(UserRequestDtoValidator userRequestDtoValidator, UserService userService, SessionService sessionService, SetSessionAttributesUtil setSessionAttributesUtil, CreateCookieUtil createCookieUtil) {
        this.userRequestDtoValidator = userRequestDtoValidator;
        this.userService = userService;
        this.setSessionAttributesUtil = setSessionAttributesUtil;
        this.createCookieUtil = createCookieUtil;
    }

    @GetMapping
    public String signIn(@ModelAttribute("userRequestDto") UserRequestDto userRequestDto) {
        return "sign-in/sign-in";
    }

    @PostMapping
    public String authorizeUser(@ModelAttribute("userRequestDto") @Valid UserRequestDto userRequestDto, BindingResult bindingResult, HttpServletRequest request, HttpServletResponse response) {

        userRequestDtoValidator.validate(userRequestDto, bindingResult);

        if (bindingResult.hasErrors())
            return "sign-in/sign-in-with-errors";

        try {
            UserResponseDto userResponseDto = userService.authorizeUser(userRequestDto);
            setSessionAttributesUtil.setSessionAttributes(request, userResponseDto);
            request.getSession();
            response.addCookie(createCookieUtil.createCookie(userResponseDto));
        } catch (UserDoesNotExistException e) {
            bindingResult.rejectValue("login", "", e.getMessage());
            return "sign-in/sign-in-with-errors";
        } catch (WrongPasswordException e) {
            bindingResult.rejectValue("password", "", e.getMessage());
            return "sign-in/sign-in-with-errors";
        }

        return "redirect:/";

    }
}

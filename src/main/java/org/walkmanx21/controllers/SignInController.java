package org.walkmanx21.controllers;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import org.walkmanx21.dto.UserRequestDto;
import org.walkmanx21.dto.UserResponseDto;
import org.walkmanx21.exceptions.UserDoesNotExistException;
import org.walkmanx21.exceptions.WrongPasswordException;
import org.walkmanx21.services.UserService;
import org.walkmanx21.util.CookieUtil;
import org.walkmanx21.util.UserRequestDtoValidatorUtil;

@Controller("/sign-in")
@RequestMapping("/sign-in")
public class SignInController {

    private final UserService userService;
    private final CookieUtil createCookieUtil;

    @Autowired
    public SignInController(UserService userService, CookieUtil createCookieUtil) {
        this.userService = userService;
        this.createCookieUtil = createCookieUtil;
    }

    @GetMapping
    public String signIn(@ModelAttribute("userRequestDto") UserRequestDto userRequestDto) {
        return "sign-in/sign-in";
    }

    @PostMapping
    public String authorizeUser(@ModelAttribute("userRequestDto") @Valid UserRequestDto userRequestDto, BindingResult bindingResult, HttpServletResponse response, HttpServletRequest request) {

        if (bindingResult.hasErrors())
            return "sign-in/sign-in-with-errors";

        try {
            UserResponseDto userResponseDto = userService.authorizeUser(userRequestDto);
            createCookieUtil.setSessionId(userResponseDto.getSessionId(), response);
        } catch (UserDoesNotExistException e) {
            bindingResult.rejectValue("login", "", e.getMessage());
            return "sign-in/sign-in-with-errors";
        } catch (WrongPasswordException e) {
            bindingResult.rejectValue("password", "", e.getMessage());
            return "sign-in/sign-in-with-errors";
        }

        Object requestUrl = request.getSession().getAttribute("requestUrl");
        if (requestUrl != null) {
            return "redirect:" + requestUrl;
        }

        return "redirect:/";

    }
}

package org.walkmanx21.controllers;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.walkmanx21.dto.UserRequestDto;
import org.walkmanx21.dto.UserResponseDto;
import org.walkmanx21.services.UserService;
import org.walkmanx21.util.CookieUtil;

@Controller("/sign-in")
@RequestMapping("/sign-in")
public class SignInController {

    private final UserService userService;
    private final CookieUtil cookieUtil;

    @Autowired
    public SignInController(UserService userService, CookieUtil cookieUtil) {
        this.userService = userService;
        this.cookieUtil = cookieUtil;
    }

    @GetMapping
    public String signIn(@ModelAttribute("userRequestDto") UserRequestDto userRequestDto) {
        return "sign-in/sign-in";
    }

    @PostMapping
    public String authorizeUser(@ModelAttribute("userRequestDto") @Valid UserRequestDto userRequestDto, BindingResult bindingResult, HttpServletRequest request) {

        if (bindingResult.hasErrors())
            return "sign-in/sign-in-with-errors";

        UserResponseDto userResponseDto = userService.authorizeUser(userRequestDto);
        request.setAttribute("sessionId", userResponseDto.getSessionId());

        if (userResponseDto.isError()) {
            bindingResult.rejectValue(userResponseDto.getErrorField(), "", userResponseDto.getErrorMessage());
            return "sign-in/sign-in-with-errors";
        }

        return "redirect:/";

    }
}

package org.walkmanx21.controllers;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.support.SessionStatus;
import org.walkmanx21.dto.UserRequestDto;
import org.walkmanx21.dto.UserResponseDto;
import org.walkmanx21.exceptions.UserDoesNotExistException;
import org.walkmanx21.exceptions.WrongPasswordException;
import org.walkmanx21.models.Session;
import org.walkmanx21.services.SessionService;
import org.walkmanx21.services.UserService;
import org.walkmanx21.util.UserRequestDtoValidator;

import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Optional;
import java.util.UUID;

@Controller("/sign-in")
@RequestMapping("/sign-in")
public class SignInController {

    private final UserRequestDtoValidator userRequestDtoValidator;
    private final UserService userService;
    @Autowired
    public SignInController(UserRequestDtoValidator userRequestDtoValidator, UserService userService, SessionService sessionService) {
        this.userRequestDtoValidator = userRequestDtoValidator;
        this.userService = userService;
    }

    @GetMapping
    public String signIn(@ModelAttribute("userRequestDto") UserRequestDto userRequestDto) {
        return "sign-in/sign-in";
    }

    @PostMapping
    public String userAuthorization(@ModelAttribute("userRequestDto") @Valid UserRequestDto userRequestDto, BindingResult bindingResult, HttpSession httpSession, HttpServletResponse response){

        userRequestDtoValidator.validate(userRequestDto, bindingResult);

        if (bindingResult.hasErrors())
            return "sign-in/sign-in-with-errors";

        try {
            Optional<UserResponseDto> mayBeUserResponseDto = userService.userAuthorization(userRequestDto);
            if (mayBeUserResponseDto.isPresent()) {
                UserResponseDto userResponseDto = mayBeUserResponseDto.get();
                httpSession.setAttribute("userId", userResponseDto.getId());
                Cookie cookie = new Cookie("sessionId", userResponseDto.getSessionId().toString());
                cookie.setMaxAge(24*60*60);
                response.addCookie(cookie);
            }
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

package org.walkmanx21.controllers;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.walkmanx21.dto.UserRequestDto;
import org.walkmanx21.services.UserService;
import org.walkmanx21.util.UserRequestDtoValidator;

@Controller("/sign-in")
@RequestMapping("/sign-in")
public class SignInController {

    private final UserRequestDtoValidator userRequestDtoValidator;
    private final UserService userService;

    @Autowired
    public SignInController(UserRequestDtoValidator userRequestDtoValidator, UserService userService) {
        this.userRequestDtoValidator = userRequestDtoValidator;
        this.userService = userService;
    }

    @GetMapping
    public String signIn(@ModelAttribute("userRequestDto")UserRequestDto userRequestDto) {
        return "sign-in/sign-in";
    }

    @PostMapping
    public String userAuthorization(@ModelAttribute("userRequestDto") @Valid UserRequestDto userRequestDto, BindingResult bindingResult){

        userRequestDtoValidator.validate(userRequestDto, bindingResult);

        if (bindingResult.hasErrors())
            return "sign-in/sign-in-with-errors";

        if (userService.userAuthorization(userRequestDto).isPresent())
            return "redirect:/";

        return "redirect:/";

    }
}

package org.walkmanx21.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller("/sign-in")
@RequestMapping("/sign-in")
public class SignInController {

    @GetMapping
    public String signIn() {
        return "/sign-in";
    }
}

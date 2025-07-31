package org.walkmanx21.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller("/sign-up")
@RequestMapping("sign-up")
public class SignUpController {

    @GetMapping
    public String signUp() {
        return "/sign-up";
    }
}

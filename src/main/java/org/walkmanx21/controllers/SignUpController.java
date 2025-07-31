package org.walkmanx21.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.walkmanx21.models.User;

@Controller("/sign-up")
@RequestMapping("sign-up")
public class SignUpController {

    @GetMapping
    public String signUp(@ModelAttribute("user") User user) {

        return "/sign-up";
    }
}

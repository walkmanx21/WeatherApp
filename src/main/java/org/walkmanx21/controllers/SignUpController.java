package org.walkmanx21.controllers;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.walkmanx21.dao.UserDao;
import org.walkmanx21.dto.UserRequestDto;

@Controller("/sign-up")
@RequestMapping("/sign-up")
public class SignUpController {

    private UserDao userDao;

    @Autowired
    public SignUpController(UserDao userDao) {
        this.userDao = userDao;
    }

    @GetMapping
    public String newUserRequestDto(@ModelAttribute("userRequestDto") UserRequestDto userRequestDto) {
        return "sign-up/sign-up";
    }

    @PostMapping
    public String signUp(@ModelAttribute("userRequestDto") @Valid UserRequestDto userRequestDto, BindingResult bindingResult) {
        if (bindingResult.hasErrors())
            return "sign-up/sign-up-with-errors";

        userDao.insertUser(userRequestDto);
        return "redirect:/index";
    }
}
